package com.example.seckill.controller;

import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.channels.SeekableByteChannel;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {

    /*
    session
    @CookieValue
     */
    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user) {
//        HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue(value = "userTicket", required = false, defaultValue = "") String uuid
//        if (StringUtils.isEmpty(uuid)) {
//            return "login";
//        }

//        User user = (User) session.getAttribute(uuid);

//        User user = userService.getUserByTicket(uuid, request, response);

        model.addAttribute("user", user);
        model.addAttribute("goodslist", goodsService.getAll());
        return "goods_list";
    }

    @RequestMapping("/toDetail/{id}")
    public String toDetail(Model model, User user, @PathVariable("id") Long id) {
//        if(user == null) {
//            return "l"
//        }
//        log.info(user.toString());
//        model.addAttribute("user", user);

        GoodsVO gvo = goodsService.getDetailById(id);
        Date beginDate = gvo.getBeginDate();
        Date endDate = gvo.getEndDate();
        Date nowdate = new Date();
        int timetobegin = beginDate.compareTo(nowdate);
        int timetoend = endDate.compareTo(nowdate);
        int seckillStatus = 0;
        // for test
        timetobegin = 1;

        if (timetobegin > 0) {
//            model.addAttribute("remainSeconds", (beginDate.getTime() - nowdate.getTime()) / 1000);\
            // for test
            model.addAttribute("remainSeconds",5);
        } else if (timetoend < 0) {
            seckillStatus = 1;
        } else {
            seckillStatus = 2;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("goods", gvo);
        model.addAttribute("user", user);
        return "goods_detail";
    }
}
