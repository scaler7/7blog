package com.scaler7.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Autowired
	private SwaggerProperty	swaggerProperty;
	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getApiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage()))
				.build();
	}

	private ApiInfo getApiInfo() {
		Contact contact = new Contact(
				swaggerProperty.getName(), 
				swaggerProperty.getUrl(), 
				swaggerProperty.getEmail()
				);
		return new ApiInfoBuilder()
				.contact(contact) // 联系人
				.title(swaggerProperty.getTitle()) // 接口的标题
				.description(swaggerProperty.getDescription()) // 接口描述
				.termsOfServiceUrl(swaggerProperty.getTermsOfServiceUrl()) // 接口的服务团队
				.license(swaggerProperty.getLicense()) // 接口授权方式
				.build();
	}
	
}
