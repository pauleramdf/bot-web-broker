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

    private String authType = "Bearer ";

    @PostConstruct
    public void construcao(){

        List<User> users = repository.findAll();
        List <String> tokens = this.getTokenUsers(users);
        List <StockDto> stocks = this.getStocks(tokens.get(0));
        List <WalletDto> validStocks;
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
            nextPriceOrder = Math.round((gerador.nextDouble(100) +1)*100.00)/100.00;
            nextVolumeOrder = gerador.nextLong(10)+1;

            if(nextOrderType == 0) {
                nextStock = gerador.nextInt(stocks.size());
                nextOrder.setIdStock(stocks.get(nextStock).getId());
                nextOrder.setStockName(stocks.get(nextStock).getStockName());
                nextOrder.setStockSymbol(stocks.get(nextStock).getStockSymbol());
            }
            else{
                validStocks = getValidStocks(tokens.get(nextUserId));
                nextStock = gerador.nextInt(validStocks.size());

                if(validStocks.get(nextStock).getVolume()>0){
                    nextOrder.setIdStock(validStocks.get(nextStock).getIdStock());
                    nextOrder.setStockName(validStocks.get(nextStock).getStockName());
                    nextOrder.setStockSymbol(validStocks.get(nextStock).getStockSymbol());

                    nextVolumeOrder = gerador.nextLong(validStocks.get(nextStock).getVolume())+1;
                }
                else{
                    continue;
                }
            }

            nextOrder.setPrice(nextPriceOrder);
            nextOrder.setVolume(nextVolumeOrder);
            nextOrder.setRemainingVolume(nextVolumeOrder);
            nextOrder.setTotalPrice(nextPriceOrder*nextVolumeOrder);
            nextOrder.setType(nextOrderType);
            
            this.criaOrdem(nextOrder, tokens.get(nextUserId));

            ordensFeitas += 1;
        }
    }

    private List<WalletDto> getValidStocks(String token){
        return Arrays.stream(
                        webClientUser.get()
                                .uri("/stockbalances")
                                .header(HttpHeaders.AUTHORIZATION, authType+ token)
                                .retrieve()
                                .bodyToMono(WalletDto[].class)
                                .block())
                .toList();
    }

    private void criaOrdem(OrderDto nextOrder, String token) {
        webClientUser
                .post()
                .uri("/order")
                .header(HttpHeaders.AUTHORIZATION, authType + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(nextOrder), OrderDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private List<StockDto> getStocks(String token){
        return Arrays.stream(
                        webClientStock.get()
                .uri("/stocks")
                .header(HttpHeaders.AUTHORIZATION, authType + token)
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

            tokens.add( gson.fromJson(token , TokenDto.class).getAccessToken());
        }

        return tokens;
    }
}

