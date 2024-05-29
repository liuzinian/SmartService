package com.liu.forever.config;



import com.liu.forever.service.WebsocketMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Resource
	private WebsocketMessageHandler taiYoWebsocketMessageHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(taiYoWebsocketMessageHandler, "/ws/info").setAllowedOrigins("*");
	}
}
