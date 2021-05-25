package com.bmd.core;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmd.core.pojo.po.BmdMsg;
import com.bmd.core.pojo.po.BmdPicture;
import com.bmd.core.service.BmdMsgSerive;
import com.bmd.core.service.BmdPictureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.*;

@SpringBootTest
@Slf4j
public class mybatisPlusTest {
    @Autowired
    private BmdPictureService bmdPictureService;

    @Autowired
    private BmdMsgSerive bmdMsgSerive;

    @Test
    public void asd() {
        Page<BmdPicture> bmdPicturePage = new Page<>(1, 2);
        Page<BmdPicture> page = bmdPictureService.page(bmdPicturePage);
        long total = page.getTotal();
        log.info("total:{}", total);
        List<BmdPicture> records = page.getRecords();
        for (BmdPicture record : records) {
            System.out.println(record);
        }
        long size = page.getSize();
        log.info("当前页条数：{}", size);

    }

    @Test
    public void asda() {
//        HashSet<String> strings = new HashSet<>();
//        strings.add("1");
//        strings.add("1");
//        for (String string : strings) {
//            System.out.println(string);
//        }
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("1");
//            }
//        },new Date(),2000);
//        QueryWrapper<BmdMsg> wrapper = new QueryWrapper<>();
//        wrapper.eq("msg_to","jack");
//        wrapper.groupBy("msg_from");
//        wrapper.select("msg_from");
//        Map<String, Object> map = bmdMsgSerive.getMap(wrapper);
//        List<Map<String, Integer>> jack = bmdMsgSerive.getChats("jack");
//        System.out.println(jack);
//        QueryWrapper<BmdMsg> wrapper = new QueryWrapper<>();
//        wrapper.and(bmdMsgQueryWrapper -> bmdMsgQueryWrapper.eq("msg_to","jack").eq("msg_from","rose"));
//        wrapper.or(bmdMsgQueryWrapper -> bmdMsgQueryWrapper.eq("msg_to","rose").eq("msg_from","jack"));
////        wrapper.and(bmdMsgQueryWrapper -> bmdMsgQueryWrapper.eq("msg_from","rose").eq("msg_to","jack"));
////        wrapper.eq("msg_from","rose").or().eq("msg_from","jack");
//        wrapper.orderByAsc("create_time");
//        wrapper.select("id","msg","msg_to","msg_from","is_read","create_time");
//        List<Map<String, Object>> maps = bmdMsgSerive.listMaps(wrapper);
//        System.out.println(maps)
//        select msg_from, count(*) from bmd_msg where msg_to = #{name} and is_read = 0 GROUP BY msg_from
//        List<String> jack = bmdMsgSerive.getChats2("jack");
//        for (String s : jack) {
//            System.out.println(s);
//        }
        List<Map<String, Object>> jack = bmdMsgSerive.getChats("jack");
        System.out.println(jack);
    }

    @Test
    public void asdasdsad() {
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 50, 4, 4);
        BufferedImage image = captcha.getImage();
        System.out.println(captcha.getCode());
    }
}
