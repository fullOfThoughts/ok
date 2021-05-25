package com.bmd.mq.service;

import cn.hutool.core.util.IdUtil;
import com.bmd.es.dao.listDao;
import com.bmd.es.po.Tutu;
import com.bmd.mq.constant.MQconst;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MQserver {
    @Autowired
    private listDao listDao;

    @RabbitListener(queuesToDeclare = @Queue(value = MQconst.QUEUE_ES, durable = "true"))
    public void recive(Tutu tutu) {
        Long ii = IdUtil.getSnowflake(1, 1).nextId();
        tutu.setId(ii);
        listDao.save(tutu);
        Optional<Tutu> byId = listDao.findById(ii);
        Tutu tutu1 = byId.get();
        System.out.println(tutu1);
    }
}
