package com.bmd.common.result;


import com.bmd.common.entity.responseEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class baseError extends RuntimeException {
    private Integer code;
    private String message;

    public baseError(responseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }
}
