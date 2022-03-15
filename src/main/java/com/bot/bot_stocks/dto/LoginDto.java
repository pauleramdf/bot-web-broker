package com.bot.bot_stocks.dto;

import com.bot.bot_stocks.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {

   private String grant_type;
   private String username;
   private String passWord;
   private String scope;

   public LoginDto(){

   }

   public LoginDto(User user){
      this.grant_type = "password";
      this.scope = "openid";
      this.username = user.getUsername();
      this.passWord = user.getPassword();
   }
}
