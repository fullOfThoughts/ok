package com.bmd.common.entity;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
public enum responseEnum {
    SUCCESS(1, "成功"),
    ERROR(0, "服务器内部错误"),
    //-1xx 服务器错误
    BAD_SQL_GRAMMAR_ERROR(-101, "sql语法错误"),
    SERVLET_ERROR(-102, "servlet请求异常"), //-2xx 参数校验
    UPLOAD_ERROR(-103, "文件上传错误"),
    EXPORT_DATA_ERROR(104, "数据导出失败"),

    //  -2xx  用户码
    USER_IS_EXITS(210, "用户名已存在"),
    USER_NO_EXITS(220, "用户不存在"),
    USER_REGISTER_FAIL(230, "用户注册失败"),

    //  -3xx  文件
    FILE_UPLOAD_ERROR(310, "文件上传失败"),
    FILE_NOT_EXITS(320, "文件不存在"),

    //  -4xx  验证码
    VERIFY_ERROR(410, "验证码错误"),
    VERIFY_EXPIRE(420, "验证码过期或不存在"),

    //  -6xx  token
    TOKEN_ERROR(610, "token效验失败"),

    //  -7xx  参数错误
    PARAM_ERROR(710, "参数有误"),

    //  -8xx  产品码
    PRODUCT_EXITS(810, "产品名称已存在"),
    PRODUCT_REMOVE_FAIL(820, "删除产品失败"),
    PICTURE_REMOVE_FAIL(830, "删除图片失败"),
    PRODUCT_NOT_EXIT(840, "产品不存在");

    private Integer code;
    private String message;
}
