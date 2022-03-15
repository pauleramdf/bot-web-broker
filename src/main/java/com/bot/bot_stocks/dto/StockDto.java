package com.bot.bot_stocks.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class StockDto{
    private Long Id;
    private String stock_symbol;
    private String stock_name;
    private Double ask_min;
    private Double ask_max;
    private Double bid_min;
    private Double bid_max;
    private Timestamp created_on;
    private Timestamp updated_on;

    public StockDto(){
    }
    public StockDto(Long id, String stock_symbol, String stock_name, Double ask_min, Double ask_max, Double bid_min, Double bid_max, Timestamp created_on, Timestamp updated_on) {
        Id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.ask_min = ask_min;
        this.ask_max = ask_max;
        this.bid_min = bid_min;
        this.bid_max = bid_max;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

}