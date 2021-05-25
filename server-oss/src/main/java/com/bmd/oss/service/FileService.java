package com.bmd.oss.service;

import com.bmd.common.result.R;

import java.io.InputStream;

public interface FileService {
    R upload(InputStream inputStream, String model, String fileName);
    R remove(String fileName);
}
