package com.bmd.oss;

import com.bmd.common.result.R;
import com.bmd.oss.service.FileService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ttt {
    @Autowired
    private FileService fileService;
    @Test
    public void  asd(){
        R remove = fileService.remove("avatar/2021/04/23/inittou.jpg");
        System.out.println(remove);
    }
}
