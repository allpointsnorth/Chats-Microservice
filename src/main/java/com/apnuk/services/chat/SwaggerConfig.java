/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The main Swagger Documentation configuration file.
 * @author gilesthompson
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))              
          .build()
          .apiInfo(apiInfo());
          
            
    }

    private ApiInfo apiInfo() {
        
        return new ApiInfoBuilder().title("Chats Microservice API")
                                   .description("API level documentation for the <b>All Points North Chats Microservice,</b> which is principally responsible for creating, managing and providing access to Chat data."
                                           + " The Chats Microservice, here described, exposes a <b>RESTful JSON based API</b> with pagination, key-based filtering and sorting"
                                           + " facilities provided and enabled by default for all data aggregation operations."
                                           + "<br>"
                                           + "<br><b>Company:</b> All Points North."
                                           + "<br><b>Web:</b> https://apnuk.com."
                                           + "<br><b>Developer:</b> Giles Thompson"
                                           + "<br><b>Contact:</b> <a href=\"mailto:giles.thompson@apnuk.com?Subject=Chat%20Microservice%20Info\" target=\"_top\">giles.thompson@apnuk.com</a>"
                                           + "<br><b>Draft Date:</b> 08/05/2018.")
                                   .version("1.0.0")
                                   .build();
    }
    
    
    
   
    
}
