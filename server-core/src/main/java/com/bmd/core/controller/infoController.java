package com.bmd.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bmd.common.result.R;
import com.bmd.common.utils.Token;
import com.bmd.core.pojo.po.BmdMsg;
import com.bmd.core.pojo.po.BmdPicture;
import com.bmd.core.pojo.po.BmdProduct;
import com.bmd.core.pojo.po.BmdUser;
import com.bmd.core.service.BmdMsgSerive;
import com.bmd.core.service.BmdPictureService;
import com.bmd.core.service.BmdProductService;
import com.bmd.core.service.BmdUserService;
import com.bmd.es.utils.getInfoUtils;
import io.swagger.annotations.Api;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api
@RequestMapping("/api/info")
public class infoController {
    @Autowired
    private BmdProductService bmdProductService;
    @Autowired
    private BmdPictureService bmdPictureService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private com.bmd.es.dao.listDao listDao;

    @Autowired
    private BmdUserService bmdUserServicel;

    @Autowired
    private BmdMsgSerive bmdMsgSerive;

    @GetMapping("/detail")
    public R getInfo(String uname, String pname) {
        //  查询头像
        QueryWrapper<BmdPicture> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("mapper_id", uname);
        wrapper2.eq("is_u", true);
        wrapper2.select("url");
        Map<String, Object> map1 = bmdPictureService.getMap(wrapper2);
        //  查询图像信息
        QueryWrapper<BmdPicture> wrapper = new QueryWrapper<>();
        wrapper.eq("mapper_id", uname);
        wrapper.eq("pid", pname);
        wrapper.select("url");
        List<Map<String, Object>> maps = bmdPictureService.listMaps(wrapper);
        //  获取产品信息
        QueryWrapper<BmdProduct> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("uid", uname);
        wrapper1.eq("pname", pname);
        wrapper1.select("uid", "pname", "pcategory", "pmoney", "pcontact", "pbaoyou", "pdesc");
        List<Map<String, Object>> maps1 = bmdProductService.listMaps(wrapper1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("urls", maps);
        map.put("info", maps1);
        map.put("avatar", map1);
        return R.ok().date(map);
    }

    @GetMapping("/tips")
    public R tips(@RequestParam(required = true) String keyword) {
        String index = "tutu";
        String key = "pname";
        if ("".equals(keyword)) {
            return R.ok().message("keyword为空");
        } else {
            return getInfoUtils.getTips(restHighLevelClient, index, key, keyword);
        }
    }

    @GetMapping("/items")
    public R items(@RequestParam(required = true) String keyword, @RequestParam(required = true) Integer page, @RequestParam(required = true) Integer pageSize) {
        //  前端页数从1算起
        return getInfoUtils.getItems(listDao, keyword, page - 1, pageSize);
    }

    @GetMapping("/getChats")
    public R getChats(HttpServletRequest request) {
        String name = getName(request);
        List<Map<String, Object>> chats = bmdMsgSerive.getChats(name);
        List<String> chats2 = bmdMsgSerive.getChats2(name);
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (String s : chats2) {
            if (chats.size() == 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("msg_from", s);
                map.put("count(*)", 0);
                list.add(map);
            } else {
                Boolean b = false;
                for (Map<String, Object> chat : chats) {
                    if (chat.get("msg_from").equals(s)) {
                        b = true;
                    }
                }
                if (b == false) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("msg_from", s);
                    map.put("count(*)", 0);
                    list.add(map);
                }
            }
        }
        chats.addAll(list);
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (Map<String, Object> chat : chats) {
            QueryWrapper<BmdUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", chat.get("msg_from"));
            wrapper.select("status", "user_name");
            Map<String, Object> map1 = bmdUserServicel.getMap(wrapper);
            maps.add(map1);
        }
        ArrayList<Map<String, Object>> list1 = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            for (Map<String, Object> chat : chats) {
                if (chat.get("msg_from").equals(map.get("user_name"))) {
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("msg_from", chat.get("msg_from"));
                    map1.put("count(*)", chat.get("count(*)"));
                    map1.put("status", map.get("status"));
                    list1.add(map1);
                }
            }
        }
        return R.ok().data("list", list1);
    }

    @GetMapping("/getMsgs")
    public R getMsgs(HttpServletRequest request, String shops) {
        String name = getName(request);
        QueryWrapper<BmdMsg> wrapper = new QueryWrapper<>();
        wrapper.and(bmdMsgQueryWrapper -> bmdMsgQueryWrapper.eq("msg_to", name).eq("msg_from", shops));
        wrapper.or(bmdMsgQueryWrapper -> bmdMsgQueryWrapper.eq("msg_to", shops).eq("msg_from", name));
        wrapper.orderByAsc("create_time");
        wrapper.select("id", "msg", "msg_to", "msg_from", "is_read", "create_time");
        List<Map<String, Object>> maps = bmdMsgSerive.listMaps(wrapper);
        for (Map<String, Object> map : maps) {
            map.put("id", map.get("id").toString());
        }
        return R.ok().data("list", maps);
    }

    @PostMapping("/readed")
    public R readed(@RequestBody BmdMsg bmdMsg) {
        for (String id : bmdMsg.getIds()) {
            UpdateWrapper<BmdMsg> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", Long.valueOf(id));
            wrapper.set("is_read", 1);
            bmdMsgSerive.update(wrapper);
        }
        return R.ok();
    }

    private String getName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return Token.getUname(token);
    }


}
