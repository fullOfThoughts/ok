package com.bmd.core.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class removeImg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String url;
}
