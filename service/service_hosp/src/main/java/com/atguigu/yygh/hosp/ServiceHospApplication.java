package com.atguigu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.atguigu")//因为不在一个包，所以在pom配置后在这里加上包路径,就可以有扫描规则就可以扫描到了
//Swagger访问网址:localhost:8080/swagger-ui.html
public class ServiceHospApplication {
    public static void main(String[] args){
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
