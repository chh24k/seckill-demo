package com.example.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.pojo.*;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.GoodsVO;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
@Controller
@RequestMapping("/seckill")
public class SeckillGoodsController implements InitializingBean {

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Long> redisScript;

    @Autowired
    private MQSender mqSender;

    // true means empty
    private Map<Long, Boolean> stockMap = new HashMap<>();


    /**
     * 返回页面
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @Deprecated
    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        // 是否还有库存
        GoodsVO detailById = goodsService.getDetailById(goodsId);
        if (detailById.getStock() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_ERROR.getMessage());
            return "seckill_fail";
        }
        model.addAttribute("goods", detailById);

        //在此处判断是否重复购买
        //之前放在了seckillorder service中
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return "seckill_fail";
        }
        Order order = orderService.doSecKill(user, detailById);
        model.addAttribute("orderInfo", order);
        return "order_detail";

    }

    @ResponseBody
    @RequestMapping(value = "/doSecKill")
    public RespBean doSecKill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.AUTH_ERROR);
        }
        // **是否还有库存**
        // 内存标记
        if (stockMap.getOrDefault(goodsId, true)) {
            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
        }

        //redis 预减库存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long decrement = valueOperations.decrement("stock:" + goodsId, 1);
//        Long decrement = (Long) redisTemplate.execute(redisScript, Collections.singletonList("stock:" + goodsId), Collections.EMPTY_LIST);
        if (decrement < 0L) {
            redisTemplate.opsForValue().increment("stock:" + goodsId, 1);
            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
        }

//        GoodsVO gvo = goodsService.getDetailById(goodsId);
//        if (gvo.getStock() < 1) {
//            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
//        }

        //在此处判断是否重复购买
        //之前放在了seckillorder service中
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id", user.getId())
//                .eq("goods_id", goodsId));

        // repeat purchase
//        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId);
//        if (seckillOrder != null) {
//            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
//        }

        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.send(JsonUtil.obj2Json(message));

        // 下单改成 发送到队列
//        Order order = orderService.doSecKill(user, gvo);
//        if (order == null) {
//            return RespBean.error(RespBeanEnum.LAST_FAIL);
//        }
        return RespBean.success(0);
    }

    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return orderId 返回成功， -1 秒杀失败， 0 正在排队
     */
    @ResponseBody
    @RequestMapping(value = "/getResult", method = RequestMethod.GET)
    public RespBean getRusult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.AUTH_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GoodsVO[] list = goodsService.getAll();
        Arrays.stream(list).forEach(good -> {
            stockMap.put(good.getId(), false);
            redisTemplate.opsForValue().set("stock:" + good.getId(), good.getStock());
        });
    }
}
