package com.test.java.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
	
	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		
		// 프롬프트 실행
		// - ChatModel
		// - ChatClient
		
		return builder.build();
	}
	
}
