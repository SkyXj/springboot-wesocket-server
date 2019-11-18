/**
* Copyright © 广州禾信仪器股份有限公司. All rights reserved.
*
* @Title: Swagger2Configuration.java
* @Package: com.xj.web.api
* @Description: TODO
* @author: sky-1012262558@qq.com
* @date: 2019-11-04 15:53:19
* @Modify Description :
* @Modify Person :
* @version: V1.0
*/

package com.xj.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket buildDocket(){
       return new Docket(DocumentationType.SWAGGER_2)
       .apiInfo(buildApiInf())
       .select()
       .apis(RequestHandlerSelectors.basePackage("com.xj.controller"))
       .paths(PathSelectors.any())
       .build();
    }

    private ApiInfo buildApiInf(){
      return new ApiInfoBuilder()
      .title("接口文档")
      .description("springboot swagger2")
    //.termsOfServiceUrl("http://blog.csdn.net/u014231523网址链接")
      .contact(new Contact(sky, "http://www.tofms.net/", "1012262558@qq.com"))
      .build();
    }

}