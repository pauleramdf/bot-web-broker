package com.bot.bot_stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {

    private Long id_stock;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Timestamp created_on;
    private Timestamp updated_on;
}
