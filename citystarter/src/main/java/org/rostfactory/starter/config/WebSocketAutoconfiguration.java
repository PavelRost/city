package org.rostfactory.starter.config;

import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

@Configuration
public class WebSocketAutoconfiguration {

    @Autowired
    private UrlConfigProperties urlConfigProperties;

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    @Bean
    public StompSessionHandler myHandler() {
        return new StompSessionHandlerImpl();
    }

    @Bean
    public StompSession stompSession (
            final StompSessionHandler sessionHandler,
            final WebSocketStompClient webSocketStompClient
    ) throws ExecutionException, InterruptedException {
        return webSocketStompClient.connectAsync(urlConfigProperties.getLogServiceUrlForWebSocket(), sessionHandler).get();
    }

}
