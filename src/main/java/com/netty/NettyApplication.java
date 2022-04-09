package com.netty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.netty.mapper")
@SpringBootApplication
public class NettyApplication {

    public static void main(String[] args) {

        SpringApplication.run(NettyApplication.class, args);
    }

}
