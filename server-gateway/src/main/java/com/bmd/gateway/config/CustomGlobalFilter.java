package com.bmd.gateway.config;

import com.bmd.common.utils.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Boolean b = false;
        ArrayList<String> list = new ArrayList<>();
        list.add("/api/login");
        list.add("/api/picture/getverify");
        list.add("/api/register");
//        list.add("/api/getToken");
//        list.add("/api/hello");
//        list.add("/api/upload");
        return chain.filter(exchange);
//        for (String s : list) {
//            if (s.equals(request.getPath().toString())) {
//                b = true;
//            }
//        }
//        System.out.println(request.getPath().toString());
//        if (b) {
//            System.out.println("脂肪");
//            return chain.filter(exchange);
//        } else {
//            String token = request.getHeaders().getFirst("token");
//
//            if (token != null  && Token.checkToken(token)) {
//                //  token通过
//                System.out.println("token通过");
//                return chain.filter(exchange);
//            } else {
//                //  token效验失败
//                System.out.println("token失败");
//                System.out.println(token);
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//        }

    }

    @Override
    public int getOrder() {            //  过滤器执行顺序，数值越小越先执行，内置过滤器数值都大于0
        return -1;
    }
}