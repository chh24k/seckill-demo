package com.example.seckill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.service.ISeckillGoodsService;
import org.apache.el.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean pathIsTrue(Long userId, Long goodsId, String path) {
        if (path == null) {
            return false;
        }
        String dp = (String) redisTemplate.opsForValue().get("path:" + userId + ":" + goodsId);
        if(dp == null) {
            return false;
        }
        return path.equals(dp);
    }

    @Override
    public String createPath(Long userId, Long goodsId) {
        String path = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("path:" + userId + ":" + goodsId, path);
        return path;
    }

    @Override
    public boolean checkCaptcha(Long userId, Long goodsId, String captcha) {
        if (captcha == null) {
            return false;
        }
        String str = (String) redisTemplate.opsForValue().get("captcha:" + userId + ":" + goodsId);
        if (str == null) {
            return false;
        }
        return str.equals(captcha);
    }
}
