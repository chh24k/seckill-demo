package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.User;
import com.example.seckill.vo.GoodsDetailVo;
import com.example.seckill.vo.GoodsVO;
import com.example.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
public interface IOrderService extends IService<Order> {

    Order doSecKill(User user, GoodsVO goods);

    OrderDetailVo getDetail(Long orderId);
}
