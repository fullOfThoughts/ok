package com.bmd.common.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class FileBean implements Serializable {
    public  static final long serialVersionUID=1L;
    private MultipartFile file;
    private String type;
}
