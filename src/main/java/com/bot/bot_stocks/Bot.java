package com.bot.bot_stocks;

import com.bot.bot_stocks.dto.OrderDto;
import com.bot.bot_stocks.dto.StockDto;
import com.bot.bot_stocks.dto.TokenDto;
import com.bot.bot_stocks.dto.WalletDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Component
public class Bot {

    private final WebClient webClientStock;
    private final WebClient webClientUser;
    private final WebClient webClientOkta;
    private int ordensFazer = 1000;
    private Random gerador = new Random();

    @Autowired
    private UserRepository repository;

    @PostConstruct
    public void construcao(){

        List<User> users = repository.findAll();
        List <String> tokens = this.getTokenUsers(users);
        List <StockDto> stocks = this.getStocks(tokens.get(0));
        List <WalletDto> validStocks = new ArrayList<>();
        OrderDto nextOrder = new OrderDto();
        int ordensFeitas = 0;
        int nextUserId = 0;
        int nextOrderType = 0;
        int nextStock = 0;
        long nextVolumeOrder = 0;
        double nextPriceOrder = 0;


        while(ordensFeitas < ordensFazer){
            nextUserId = gerador.nextInt(users.size());
            nextOrderType = gerador.nextInt(2);
            nextPriceOrder = gerador.nextDouble(100) +1;
            nextVolumeOrder = gerador.nextLong(10)+1;

            if(nextOrderType == 0) {
                nextStock = gerador.nextInt(stocks.size());
                nextOrder.setId_stock(stocks.get(nextStock).getId());
                nextOrder.setStock_name(stocks.get(nextStock).getStock_name());
                nextOrder.setStock_symbol(stocks.get(nextStock).getStock_symbol());
            }
            else{
                validStocks = getValidStocks(tokens.get(nextUserId));
                nextStock = gerador.nextInt(validStocks.size());

                if(validStocks.get(nextStock).getVolume()>0){
                    nextOrder.setId_stock(validStocks.get(nextStock).getId_stock());
                    nextOrder.setStock_name(validStocks.get(nextStock).getStock_name());
                    nextOrder.setStock_symbol(validStocks.get(nextStock).getStock_symbol());

                    nextVolumeOrder = gerador.nextLong(validStocks.get(nextStock).getVolume())+1;
                }
                else{
                    continue;
                }
            }

            nextOrder.setPrice(nextPriceOrder);
            nextOrder.setVolume(nextVolumeOrder);
            nextOrder.setRemaining_volume(nextVolumeOrder);
            nextOrder.setTotal_price(nextPriceOrder*nextVolumeOrder);
            nextOrder.setType(nextOrderType);
            
            this.criaOrdem(nextOrder, tokens.get(nextUserId));

//            System.out.println("bot do user" + nextUserId + " "+ ordensFeitas + " " + Date.from(Instant.now()));
            ordensFeitas += 1;
        }
    }

    private List<WalletDto> getValidStocks(String token){
        return Arrays.stream(
                        webClientUser.get()
                                .uri("/stockbalances")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                                .retrieve()
                                .bodyToMono(WalletDto[].class)
                                .block())
                .toList();
    }

    private void criaOrdem(OrderDto nextOrder, String token) {
        String ordem = webClientUser
                .post()
                .uri("/order")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(nextOrder), OrderDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(ordem);
    }

    private List<StockDto> getStocks(String token){
        return Arrays.stream(
                        webClientStock.get()
                .uri("/stocks")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .retrieve()
                .bodyToMono(StockDto[].class)
                .block())
                .toList();
    }

    private List<String> getTokenUsers(List<User> users) {
        List <String> tokens = new ArrayList<>();
        Gson gson = new Gson();

        for (User user : users) {

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", user.getUsername());
            formData.add("password", user.getPassword());
            formData.add("scope", "openid");
            formData.add("grant_type", "password");

            String token = webClientOkta.post()
                    .uri("/")
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


//            System.out.println(gson.fromJson(token , TokenDto.class).toString());

            tokens.add( gson.fromJson(token , TokenDto.class).getAccess_token());
        }

        for (int i = 0; i < users.size() ; i++) {
            System.out.println(tokens.get(i));
        }
        return tokens;
    }
}

