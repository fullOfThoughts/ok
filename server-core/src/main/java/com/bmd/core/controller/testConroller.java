package com.bmd.core.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bmd.common.utils.Token;
import com.bmd.core.pojo.po.BmdMsg;
import com.bmd.core.pojo.po.BmdUser;
import com.bmd.core.pojo.vo.Shout;
import com.bmd.core.service.BmdMsgSerive;
import com.bmd.core.service.BmdUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@Slf4j
public class testConroller {
    //  已登录列表
    private static Map<String, Long> map = new HashMap<>();
    @Autowired
    private BmdUserService bmdUserService;
    @Autowired
    private BmdMsgSerive bmdMsgSerive;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    public testConroller() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkHealth();
            }
        }, new Date(), 1000 * 60 * 4);

    }

    @MessageMapping("/message")
    public void handleShout(BmdMsg bmdMsg) {
        String uname = Token.getUname(bmdMsg.getToken());
        bmdMsg.setMsgFrom(uname);
        bmdMsg.setId(IdUtil.getSnowflake(1, 1).nextId());
        //  入库
        System.out.println(bmdMsg);
        bmdMsgSerive.save(bmdMsg);
        //  发送消息
        HashMap<String, String> map = new HashMap<>();
        map.put("message", bmdMsg.getMsg());
        map.put("id", String.valueOf(bmdMsg.getId()));
        map.put("msg_from", bmdMsg.getMsgFrom());
        simpMessageSendingOperations.convertAndSendToUser(bmdMsg.getMsgTo(), "/message", map);
    }

    @MessageMapping("/login")
    public void handLogin(BmdMsg bmdMsg) {
        String uname = Token.getUname(bmdMsg.getToken());
        map.put(uname, System.currentTimeMillis());
        UpdateWrapper<BmdUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_name", uname);
        wrapper.set("status", 1);
        bmdUserService.update(wrapper);
        System.out.println(uname + "------登录成功");
    }

    @MessageMapping("/keepHealth")
    public void keepHealth(BmdMsg bmdMsg) {
        String uname = Token.getUname(bmdMsg.getToken());
        map.put(uname, System.currentTimeMillis());
    }


    private void checkHealth() {
        Set<String> keySet = map.keySet();
        ArrayList<String> list = new ArrayList<>();
        for (String key : keySet) {
            if (System.currentTimeMillis() - map.get(key) > 1000 * 60 * 5) {
                //  表示掉线
                list.add(key);
            }
        }
        for (String key : list) {
            map.remove(key);
            UpdateWrapper<BmdUser> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_name", key);
            wrapper.set("status", 0);
            bmdUserService.update(wrapper);
            System.out.println(key + "已掉线");
        }
    }


}