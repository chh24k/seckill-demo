package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.SeckillGoods;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {

    boolean pathIsTrue(Long userId, Long goodsId, String path);

    String createPath(Long id, Long goodsId);

    boolean checkCaptcha(Long id, Long goodsId, String captcha);
}
