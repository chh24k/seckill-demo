package com.example.seckill.rabbitmq;

import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillMessage;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
//
public class MQReceiver {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {

        //此处仍要判断是否有库存
        log.info("Receive:" + msg);
        SeckillMessage seckillMessage = JsonUtil.json2Obj(msg, SeckillMessage.class);
        GoodsVO gvo = goodsService.getDetailById(seckillMessage.getGoodsId());
        if (gvo.getStock() < 1) {
            return;
        }
        //是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + seckillMessage.getUser().getId() + ":" + seckillMessage.getGoodsId());
        if (seckillOrder != null) {
            return;
        }
        orderService.doSecKill(seckillMessage.getUser(), gvo);
    }
}
