package com.example.seckill.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.mapper.SeckillOrderMapper;
import com.example.seckill.pojo.*;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;


    //之前传的是 Long goodsId 如果你已经查出了GoodsVo 那么就传进来
    @Override
    @Transactional
    public Order doSecKill(User user, GoodsVO goods) {
        //获取对应货品库存 更新
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillGoods seckillGoods = seckillGoodsService.getById(goods.getId());
        if (seckillGoods.getStock() < 1) {
            throw new GlobalException(RespBeanEnum.EMPTY_ERROR);
        }
//        seckillGoods.setStock(seckillGoods.getStock() - 1);
        boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock = stock - 1")
                .eq("id", seckillGoods.getId())
                .gt("stock", 0)
        );
        if (!update) {
            valueOperations.set("isStockEmpty:" + goods.getId(), 0);
            return null;
        }
        //创建订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(Long.parseLong("123456789"));
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrices(goods.getSecKillPrice());
        order.setOrderChannel(1);
        order.setOrderStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderMapper.insert(seckillOrder);


        valueOperations.set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
        return order;
    }


    /**
     * 在service层应该去操作数据库了 不应该再去缓存中取库存了?
     *
     * @Override
     * @Transactional public void doSecKill(User user, Long goodsId) {
     * ValueOperations valueOperations = redisTemplate.opsForValue();
     * GoodsVO gvo = goodsService.getDetailById(goodsId);
     * if (gvo.getStock() < 1) {
     * valueOperations.set("user:" + user.getId() + goodsId, 0L);
     * return;
     * }
     * boolean update = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
     * .setSql("stock = stock - 1")
     * .eq("id", goodsId)
     * .gt("stock", 0)
     * );
     * if (!update) {
     * valueOperations.set("user:" + user.getId() + goodsId, 0L);
     * return;
     * }
     * //创建订单
     * Order order = new Order();
     * order.setUserId(user.getId());
     * order.setGoodsId(goodsId);
     * order.setDeliveryAddrId(Long.parseLong("123456789"));
     * order.setGoodsName(gvo.getGoodsName());
     * order.setGoodsCount(1);
     * order.setGoodsPrices(gvo.getSecKillPrice());
     * order.setOrderChannel(1);
     * order.setOrderStatus(0);
     * order.setCreateDate(new Date());
     * orderMapper.insert(order);
     * <p>
     * SeckillOrder seckillOrder = new SeckillOrder();
     * seckillOrder.setOrderId(order.getId());
     * seckillOrder.setUserId(user.getId());
     * seckillOrder.setGoodsId(goodsId);
     * seckillOrderMapper.insert(seckillOrder);
     * <p>
     * valueOperations.set("order:" + user.getId() + ":" + goodsId, seckillOrder);
     * }
     */

    @Override
    public OrderDetailVo getDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        GoodsVO gvo = goodsService.getDetailById(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoods(gvo);
        return detail;
    }
}
