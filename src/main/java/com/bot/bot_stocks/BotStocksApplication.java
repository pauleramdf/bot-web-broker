package com.bot.bot_stocks;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BotStocksApplication {

    private final Bot bot;

    public static void main(String[] args) {
        SpringApplication.run(BotStocksApplication.class, args);
    }


}
