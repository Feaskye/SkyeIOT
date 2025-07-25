package com.example.serverc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.example.common.*"})
@MapperScan("com.example.common.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class ServerCApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCApplication.class, args);
    }

}
