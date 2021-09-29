package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //General
    SUCCESS(200,"SUCCESS"),
    ERROR(200,"EXCEPTION IN SERVER"),
    //Login
    LOGIN_ERROR(500210,"WRONG INPUT"),
    MOBILE_ERROR(500211, "NOT A PHONE"),
    //For validation
    PASSWORD_ERROR(500212,"WRONG PASSWORD"),
    //For seckill
    EMPTY_ERROR(500301,"EMPTY STOCK"),
    REPEAT_ERROR(500302,"REPEAT PURCHASE");

    private final Integer code;
    private final String message;

}
