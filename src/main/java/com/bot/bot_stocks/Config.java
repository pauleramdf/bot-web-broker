package com.bot.bot_stocks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackageClasses = Bot.class)
public class Config {

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    @Bean
    public WebClient webClientStock(WebClient.Builder builder){
//        .baseUrl("http://apistocks:8083") to docker;
//        .baseUrl("http://localhost:8083") to localhost
        return  builder
                .baseUrl("http://localhost:8083")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient webClientUser(WebClient.Builder builder){
//        .baseUrl("http://apiusers:8082") to docker;
//        .baseUrl("http://localhost:8082") to localhost
        return  builder
                .baseUrl("http://localhost:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient webClientOkta(WebClient.Builder builder){
        return  builder
                .baseUrl("https://dev-8471287.okta.com/oauth2/default/v1/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeaders(header -> header.setBasicAuth(clientId, clientSecret))
                .build();
    }
}
