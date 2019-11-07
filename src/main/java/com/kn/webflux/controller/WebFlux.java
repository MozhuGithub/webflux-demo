package com.kn.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName: WebFlux
 * @Description TODO:
 * @Date: 2019/11/2 17:11
 * @Author: Kn
 */
@RestController
@Slf4j
public class WebFlux {

    private final UserService userService;

    public WebFlux(UserService userService) {
        this.userService = userService;
    }

    public String getMessage() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "服务器反馈的信息";
    }
    //传统方式
    @RequestMapping("/1")
    public String traditionalWay() {
        log.info("请求开始时间："+ LocalDateTime.now());
        String message =getMessage();
        log.info("请求结束时间："+LocalDateTime.now());
        return message;
    }
    //Mono 方式  返回0-1个序列
    @RequestMapping("/2")
    public Mono<String> monoWay() {
        log.info("请求开始时间："+ LocalDateTime.now());
        Mono<String> message = Mono.fromSupplier(() -> getMessage());
        log.info("请求结束时间："+LocalDateTime.now());
        return message;
    }

    //flux 方式 返回多个序列
    @RequestMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> fluxWay() {
        Flux<String> message = Flux.fromStream(IntStream.range(1,10).mapToObj(
                i->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "flux message:" + i;
                }
        ));
        return message;
    }

    @RequestMapping("/4")
    public ResponseEntity<String> runtimeException(){
       return userService.run1();
    }

}
