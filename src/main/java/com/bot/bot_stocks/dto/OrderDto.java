package com.bot.bot_stocks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long id_stock;
    private String stock_name;
    private String stock_symbol;
    private Long volume;
    private Double price;
    private Long remaining_volume;
    private Double total_price;
    private Integer type;
}
