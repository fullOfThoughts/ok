package com.bmd.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmd.core.mapper.BmdMsgMapper;

import com.bmd.core.pojo.po.BmdMsg;

import com.bmd.core.service.BmdMsgSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BmdMsgServiceImpl extends ServiceImpl<BmdMsgMapper, BmdMsg> implements BmdMsgSerive {
    @Autowired
    private BmdMsgMapper bmdMsgMapper;

    @Override
    public List<Map<String, Object>> getChats(String name) {
        return bmdMsgMapper.getChats(name);
    }

    @Override
    public List<String> getChats2(String name) {
        return bmdMsgMapper.getChats2(name);
    }
}
