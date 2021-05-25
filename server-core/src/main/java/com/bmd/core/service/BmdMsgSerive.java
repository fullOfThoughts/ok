package com.bmd.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmd.core.pojo.po.BmdMsg;
import com.bmd.core.pojo.po.BmdUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  卡卡卡
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
public interface BmdMsgSerive extends IService<BmdMsg> {
    List<Map<String,Object>> getChats(String name);
    List<String> getChats2(String name);
}
