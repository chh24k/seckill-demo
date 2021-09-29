package com.example.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckill.pojo.Goods;
import com.example.seckill.vo.GoodsVO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author honghui
 * @since 2021-09-28
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    GoodsVO[] getAll();

    GoodsVO getDetailById(Long id);
}
