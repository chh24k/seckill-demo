package com.example.seckill.vo;

import com.example.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Honghui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetailVo {

    private GoodsVO goods;
    private User user;
    private Long remainSeconds;
    private Integer secKillStatus;

}
