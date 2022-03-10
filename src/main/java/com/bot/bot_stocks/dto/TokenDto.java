package com.bot.bot_stocks.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDto {
    private String token_type;
    private String access_token;
    private Long expires_in;
    private String scope;
    private String id_token;

    public TokenDto(){}

    @Override
    public String toString() {
        return "TokenDto{" +
                "token_type='" + token_type + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", scope='" + scope + '\'' +
                ", id_token='" + id_token + '\'' +
                '}';
    }
}
