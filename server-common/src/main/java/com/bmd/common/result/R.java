package com.bmd.common.result;

import com.bmd.common.entity.responseEnum;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class R {
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.code = responseEnum.SUCCESS.getCode();
        r.message = responseEnum.SUCCESS.getMessage();
        return r;
    }

    public static R error() {
        R r = new R();
        r.code = responseEnum.ERROR.getCode();
        r.message = responseEnum.ERROR.getMessage();
        return r;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R date(Map<String, Object> map) {
        this.data = map;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public static R setResponseEnum(responseEnum res) {
        R r = new R();
        r.code = res.getCode();
        r.message = res.getMessage();
        return r;
    }

    public static R baseError(baseError baseError) {
        R r = new R();
        r.code = baseError.getCode();
        r.message = baseError.getMessage();
        return r;
    }


}
