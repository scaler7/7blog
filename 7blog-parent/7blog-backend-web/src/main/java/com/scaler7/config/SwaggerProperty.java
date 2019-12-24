package com.scaler7.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "swagger2")
public class SwaggerProperty {
	
	private String basePackage;
	private String name;
	private String url;
	private String email;
	private String title;
	private String description;
	private String termsOfServiceUrl;
	private String license;

}
