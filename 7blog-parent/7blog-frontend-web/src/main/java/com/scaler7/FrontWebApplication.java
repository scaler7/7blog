package com.scaler7;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.scaler7.mapper")
public class FrontWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FrontWebApplication.class, args);
	}
	
}
