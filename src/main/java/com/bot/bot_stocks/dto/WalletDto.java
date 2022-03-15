package com.bot.bot_stocks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {

    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Timestamp created;
    private Timestamp updated;
}
