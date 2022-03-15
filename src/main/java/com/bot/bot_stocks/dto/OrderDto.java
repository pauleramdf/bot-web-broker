package com.bot.bot_stocks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long idStock;
    private String stockName;
    private String stockSymbol;
    private Long volume;
    private Double price;
    private Long remainingVolume;
    private Double totalPrice;
    private Integer type;
}
