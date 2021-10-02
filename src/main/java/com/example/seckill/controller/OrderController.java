package com.example.seckill.controller;


import com.example.seckill.config.UserArgumentResolver;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IOrderService;
import com.example.seckill.vo.GoodsDetailVo;
import com.example.seckill.vo.OrderDetailVo;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/getOrder/{id}")
    @ResponseBody
    public RespBean getOrder(User user, @PathVariable("id") Long orderId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.AUTH_ERROR);
        }
        OrderDetailVo detail = orderService.getDetail(orderId);
        return RespBean.success(detail);
    }

}
