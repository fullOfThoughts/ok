package com.bmd.core.controller;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmd.common.entity.responseEnum;
import com.bmd.common.result.R;
import com.bmd.common.utils.Token;
import com.bmd.core.pojo.po.BmdPicture;
import com.bmd.core.pojo.po.BmdProduct;
import com.bmd.core.pojo.po.BmdUser;
import com.bmd.core.service.BmdPictureService;
import com.bmd.core.service.BmdProductService;
import com.bmd.core.service.BmdUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/api")
@Api(tags = "user")
public class BmdUserController {
    @Autowired
    private BmdUserService bmdUserService;
    @Autowired
    private BmdPictureService bmdPictureService;
    @Autowired
    private BmdProductService bmdProductService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public R save(@RequestBody BmdUser bmdUser) {
        //  验证码
        R verify = verify(bmdUser);
        if (!verify.getCode().equals(1)) {
            return verify;
        }

        //  保存
        try {
            QueryWrapper<BmdUser> bmdUserQueryWrapper = new QueryWrapper<>();
            bmdUserQueryWrapper.eq("user_name", bmdUser.getUserName());
            List<BmdUser> list = bmdUserService.list(bmdUserQueryWrapper);
            if (list.size() > 0) {
                return R.setResponseEnum(responseEnum.USER_IS_EXITS);
            }
            bmdUserService.save(bmdUser);
            //  初始化图片数据
            BmdPicture bmdPicture = new BmdPicture();
            bmdPicture.setU(true);
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            bmdPicture.setId(snowflake.nextId());
            bmdPicture.setMapperId(bmdUser.getUserName());
            bmdPicture.setPid("");
            bmdPictureService.save(bmdPicture);
            return R.ok().message("注册成功");
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @GetMapping("/remove")
    public R remove(String username) {
        try {
            QueryWrapper<BmdUser> bmdUserQueryWrapper = new QueryWrapper<>();
            bmdUserQueryWrapper.eq("user_name", username);
            BmdUser one = bmdUserService.getOne(bmdUserQueryWrapper);
            bmdUserService.removeById(one.getId());
            return R.ok().message("删除成功");
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody BmdUser bmdUser) {
        try {
            QueryWrapper<BmdUser> bmdUserQueryWrapper = new QueryWrapper<>();
            bmdUserQueryWrapper.eq("user_name", bmdUser.getUserName());
            BmdUser one = bmdUserService.getOne(bmdUserQueryWrapper);
            bmdUser.setId(one.getId());
            bmdUserService.updateById(bmdUser);
            return R.ok().message("更新成功");
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/login")
    public R login(@RequestBody BmdUser bmdUser) {
        //  验证码
        R verify = verify(bmdUser);
        if (!verify.getCode().equals(1)) {
            return verify;
        }
        //  登录
        QueryWrapper<BmdUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", bmdUser.getUserName());
        BmdUser one = bmdUserService.getOne(wrapper);
        if (one == null) {
            return R.setResponseEnum(responseEnum.USER_NO_EXITS);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("userName", bmdUser.getUserName());
            return R.ok().data("token", Token.getToken(map, 30));
        }
    }

    @GetMapping("/getToken")
    public R getToken(HttpServletRequest request) {
        String uname = getName(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", uname);
        return R.ok().data("token", Token.getToken(map, 30));
    }

    @GetMapping("/info")
    public R getInfo(HttpServletRequest request) {
        //  查询用户信息
        String uname = getName(request);
        QueryWrapper<BmdUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", uname);
        BmdUser one = bmdUserService.getOne(wrapper);
        if (one == null) {
            return R.setResponseEnum(responseEnum.USER_NO_EXITS);
        } else {
            //  查询头像地址
            QueryWrapper<BmdPicture> bmdPictureQueryWrapper = new QueryWrapper<>();
            bmdPictureQueryWrapper.eq("mapper_id", one.getUserName());
            bmdPictureQueryWrapper.eq("is_u", true);
            BmdPicture one1 = bmdPictureService.getOne(bmdPictureQueryWrapper);
            //  查询用户拥有的产品数
            QueryWrapper<BmdProduct> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("uid", one.getUserName());
            int count = bmdProductService.count(wrapper1);

            //  封装结果
            HashMap<String, Object> map = new HashMap<>();
            map.put("userName", one.getUserName());
            map.put("gender", one.getGender());
            map.put("marry", one.getMarry());
            if (one1 != null) {
                map.put("avatar", one1.getUrl());
            } else {
                map.put("avatar", "");
            }
            map.put("pcount", count);
            return R.ok().date(map);
        }
    }

    @GetMapping("/getName")
    public  R getname(HttpServletRequest request){
        return R.ok().data("name",getName(request));
    }


    private String getName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return Token.getUname(token);
    }


    // 验证验证码
    private R verify(BmdUser bmdUser) {
        Boolean aBoolean = stringRedisTemplate.hasKey(bmdUser.getUuid());
        if (aBoolean) {
            if (!stringRedisTemplate.opsForValue().get(bmdUser.getUuid()).equalsIgnoreCase(bmdUser.getVerifyCode())) {
                return R.setResponseEnum(responseEnum.VERIFY_ERROR);
            } else {
                return R.ok();
            }
        } else {
            return R.setResponseEnum(responseEnum.VERIFY_EXPIRE);
        }
    }


}

