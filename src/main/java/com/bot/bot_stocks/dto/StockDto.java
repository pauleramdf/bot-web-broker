package com.bot.bot_stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDto{
    private Long id;
    private String stockSymbol;
    private String stockName;
    private Double askMin;
    private Double askMax;
    private Double bidMin;
    private Double bidMax;
    private Timestamp created;
    private Timestamp updated;

}