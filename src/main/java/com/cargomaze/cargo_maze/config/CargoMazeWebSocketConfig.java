package com.cargomaze.cargo_maze.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class CargoMazeWebSocketConfig implements WebSocketMessageBrokerConfigurer  {
    private static final String[] ALLOWED_ORIGINS = {
            "http://localhost:4200",
            "https://ashy-flower-01560480f.4.azurestaticapps.net",
            
    };

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");        
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stompendpoint")
                .setAllowedOriginPatterns(ALLOWED_ORIGINS)
                .withSockJS();
    }
}
