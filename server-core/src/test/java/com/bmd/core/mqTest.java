package com.bmd.core;
import com.bmd.es.po.Tutu;
import com.bmd.mq.constant.MQconst;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void as(){
        Tutu tutu = new Tutu();
        tutu.setPname("nihao");
        rabbitTemplate.convertAndSend(MQconst.QUEUE_ES,tutu);
    }
}
