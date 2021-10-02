package com.example.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.exception.GlobalException;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.service.Impl.SeckillOrderServiceImpl;
import com.example.seckill.vo.GoodsVO;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class SeckillGoodsController {

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;


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
    @RequestMapping("/doSecKill")
    public RespBean doSecKill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.AUTH_ERROR);
        }
        // 是否还有库存
        GoodsVO gvo = goodsService.getDetailById(goodsId);
        if (gvo.getStock() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_ERROR);
        }
        //在此处判断是否重复购买
        //之前放在了seckillorder service中
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id", user.getId())
//                .eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        Order order = orderService.doSecKill(user, gvo);
        if (order == null) {
            return RespBean.error(RespBeanEnum.LAST_FAIL);
        }
        return RespBean.success(order);
    }

}
