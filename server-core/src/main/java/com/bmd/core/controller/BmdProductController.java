package com.bmd.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmd.common.entity.responseEnum;
import com.bmd.common.result.R;
import com.bmd.common.utils.Token;
import com.bmd.core.pojo.po.BmdProduct;
import com.bmd.core.service.BmdProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/product")
public class BmdProductController {
    @Autowired
    private BmdProductService bmdProductService;

    @PostMapping("/save")
    public R save(HttpServletRequest request, @RequestBody BmdProduct bmdProduct) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        bmdProduct.setUid(uname);
        //  判断产品名称是否存在
        QueryWrapper<BmdProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("pname", bmdProduct.getPname());
        wrapper.eq("uid", bmdProduct.getUid());
        List<BmdProduct> list = bmdProductService.list(wrapper);
        if (list.size() > 0) {
            return R.setResponseEnum(responseEnum.PRODUCT_EXITS);
        }
        //  保存
        bmdProductService.save(bmdProduct);
        return R.ok();
    }

    @GetMapping("/list")
    public R list(HttpServletRequest request) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        QueryWrapper<BmdProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uname);
        wrapper.select("pname", "pcategory", "pmoney", "pbaoyou");
        List<Map<String, Object>> maps = bmdProductService.listMaps(wrapper);
        return R.ok().data("list", maps);
    }

    @GetMapping("/remove")
    public R remove(HttpServletRequest request, String pname) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        Boolean aBoolean = bmdProductService.removePandI(uname, pname);
        if (aBoolean) {
            return R.ok();
        } else {
            return R.setResponseEnum(responseEnum.PICTURE_REMOVE_FAIL);
        }
    }

    @GetMapping("/removeOnly")
    public R removeOnle(HttpServletRequest request, String pname) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        QueryWrapper<BmdProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uname);
        wrapper.eq("pname", pname);
        List<BmdProduct> list = bmdProductService.list(wrapper);
        if (list.size() > 0) {
            bmdProductService.remove(wrapper);
            return R.ok();
        } else {
            return R.setResponseEnum(responseEnum.PRODUCT_NOT_EXIT);
        }

    }


    @GetMapping("/getOne")
    public R getOne(HttpServletRequest request, String pname) {
        String token = request.getHeader("token");
        String uname = Token.getUname(token);
        QueryWrapper<BmdProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uname);
        wrapper.eq("pname", pname);
        wrapper.select("pname", "pcategory", "pmoney", "pbaoyou", "pcontact", "pdesc");
        Map<String, Object> map = bmdProductService.getMap(wrapper);
        return R.ok().date(map);

    }
}

