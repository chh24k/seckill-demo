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
    LOGIN_ERROR(500210,"NOT REGISTER"),
    MOBILE_ERROR(500211, "NOT A PHONE"),
    //For validation
    PASSWORD_ERROR(500212,"WRONG PASSWORD"),
    //For seckill
    EMPTY_ERROR(500301,"EMPTY STOCK"),
    REPEAT_ERROR(500302,"REPEAT PURCHASE"),
    PATH_IS_ILLEGAL(500402, "PATH IS ILLEGAL"),
    REQUEST_ILLEGAL(500403,"REQUEST ILLEGAL"),
    CAPTCHA_WRONG(500505,"CAPTCHA WRONG"),
    ACCESS_LIMIT(500808,"TOO FREQUENT"),

    //For order

    AUTH_ERROR(500401,"USER NOT LOGIN"),
    ORDER_NOT_EXIST(500402,"ORDER NOT EXIST"),
    LAST_FAIL(500404,"LAST FAIL");
    private final Integer code;
    private final String message;

}
