package com.bmd.oss;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bmd.oss","com.bmd.common.config"})
public class OssApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(OssApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
