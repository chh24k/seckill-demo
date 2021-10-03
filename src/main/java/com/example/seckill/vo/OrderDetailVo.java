package com.example.seckill.vo;

import com.example.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private GoodsVO goods;
    private Order order;
}
