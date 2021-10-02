package com.example.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReciever {

//    @RabbitListener(queues = "queue")
//    public void recieve(Object obj) {
//        log.info(obj.toString());
//    }

    @RabbitListener(queues = "seckillQueue")
    public Long recieve(Message msg) {
        return null;
    }
}
