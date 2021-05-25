package com.bmd.core.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bmd.common.entity.FileBean;
import com.bmd.common.entity.responseEnum;
import com.bmd.common.result.R;
import com.bmd.common.utils.Token;
import com.bmd.core.feign.ossClient;
import com.bmd.core.pojo.vo.removeImg;
import com.bmd.core.pojo.po.BmdPicture;
import com.bmd.core.service.BmdPictureService;
import com.bmd.core.service.BmdUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/api/picture")
@Slf4j
public class BmdPictureController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ossClient ossClient;
    @Autowired
    private BmdUserService bmdUserService;

    @Autowired
    private BmdPictureService bmdPictureService;

    @GetMapping("/getverify")
    public R getverify() {
        try {
            //  获取img的base64
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 50, 4, 4);
            BufferedImage verifyImg = captcha.getImage();
            ByteArrayOutputStream opt = new ByteArrayOutputStream();
            //  创建流
            ImageIO.write(verifyImg, "png", opt);         //  写入流
            byte[] bytes = opt.toByteArray();
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String trim = base64Encoder.encodeBuffer(bytes).trim();
            String s = "data:image/jpg;base64," + trim.replaceAll("\n", "").replaceAll("\r", "");
            //  存入redis
            String replace = UUID.randomUUID().toString().replace("-", "");
            stringRedisTemplate.opsForValue().set(replace, captcha.getCode(), 60, TimeUnit.SECONDS);
            HashMap<String, Object> map = new HashMap<>();
            map.put("src", s);
            map.put("uuid", replace);
            return R.ok().date(map);
        } catch (IOException e) {
            return R.error().message(e.getMessage());
        }
    }

    /**
     * @param request
     * @param file
     * @param type：avatar or product
     * @param otherInfo   type为avatar时它是旧的URL，type为product时它为商品名称
     * @return
     */
    @PostMapping("/upload")
    public R upload(HttpServletRequest request, MultipartFile file, String type, String otherInfo) {
        if (file == null || type == null || otherInfo == null || !(type.equals("avatar") || type.equals("product"))) {
            return R.setResponseEnum(responseEnum.PARAM_ERROR);
        }

        FileBean fileBean = new FileBean();
        fileBean.setFile(file);
        fileBean.setType(type);

        //  头像删除旧地址
        if (type.equals("avatar")) {
            removeAvatar(otherInfo);
        }
        //  上传文件
        R upload = ossClient.upload(fileBean);
        if (!upload.getCode().equals(1)) {
            return upload;
        }
        //  更新数据库
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        if (type.equals("avatar")) {
            UpdateWrapper<BmdPicture> wrapper = new UpdateWrapper<>();
            wrapper.set("url", upload.getData().get("path"));
            wrapper.eq("mapper_id", uname);
            wrapper.eq("is_u", 1);
            bmdPictureService.update(wrapper);
            return R.ok().data("path", upload.getData().get("path"));
        } else {
            BmdPicture bmdPicture = new BmdPicture();
            bmdPicture.setUrl(upload.getData().get("path").toString());
            bmdPicture.setU(false);
            bmdPicture.setMapperId(uname);

            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            bmdPicture.setId(snowflake.nextId());
            bmdPicture.setPid(otherInfo);
            bmdPictureService.save(bmdPicture);
            return R.ok();
        }
    }

    @GetMapping("/getUrl")
    public R getUrl(HttpServletRequest request, String pname) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        QueryWrapper<BmdPicture> wrapper = new QueryWrapper<>();
        wrapper.eq("mapper_id", uname);
        wrapper.eq("pid", pname);

        wrapper.select("url", "id");
        List<Map<String, Object>> maps = bmdPictureService.listMaps(wrapper);
        //  Long--->String
        maps.forEach(map -> {
            map.put("id", map.get("id").toString());
        });
        return R.ok().data("list", maps);
    }

    @PostMapping("/remove")
    public R remove(@RequestBody List<removeImg> imgs) {
        imgs.forEach(val -> {
            bmdPictureService.removeById(val.getId());
            removeAvatar(val.getUrl());
        });
        return R.ok();
    }


    @PostMapping("/update")
    public R update(HttpServletRequest request,String oP,String nP){
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        UpdateWrapper<BmdPicture> wrapper = new UpdateWrapper<>();
        wrapper.eq("mapper_id",uname);
        wrapper.eq("pid",oP);
        wrapper.set("pid",nP);
        bmdPictureService.update(wrapper);
        return R.ok();
    }

    private void removeAvatar(String fileName) {
        String name = fileName.substring(fileName.indexOf(".com/") + 5);
        ossClient.remove(name);
    }

}

