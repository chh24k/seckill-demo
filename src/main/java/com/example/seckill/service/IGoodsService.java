package com.example.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
public interface IGoodsService extends IService<Goods> {

    GoodsVO[] getAll();

    GoodsVO getDetailById(Long id);
}
