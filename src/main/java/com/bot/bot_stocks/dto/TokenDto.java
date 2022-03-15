package com.bot.bot_stocks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TokenDto {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("id_token")
    private String idToken;

    @Override
    public String toString() {
        return "TokenDto{" +
                "token_type='" + tokenType + '\'' +
                ", access_token='" + accessToken + '\'' +
                ", expires_in=" + expiresIn +
                ", scope='" + scope + '\'' +
                ", id_token='" + idToken + '\'' +
                '}';
    }
}
