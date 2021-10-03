package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * orderId 返回成功， -1 秒杀失败， 0 正在排队
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
