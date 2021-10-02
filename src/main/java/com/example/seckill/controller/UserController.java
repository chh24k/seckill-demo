package com.example.seckill.controller;


import com.example.seckill.pojo.User;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author honghui
 * @since 2021-09-27
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        if (user == null) {
            return null;
        }
        log.info("User info", user);
        return RespBean.success(user);
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public RespBean mq(User user) {
//        mqSender.send("hello world!");
//        return RespBean.success(new User());
//    }

}
