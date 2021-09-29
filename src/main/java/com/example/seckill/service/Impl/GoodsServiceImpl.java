package com.example.seckill.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public GoodsVO[] getAll() {
        return goodsMapper.getAll();
    }

    @Override
    public GoodsVO getDetailById(Long id) {
        return goodsMapper.getDetailById(id);
    }
}
