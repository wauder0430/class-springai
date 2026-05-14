package com.test.java.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AiConfig {
	
//	@Bean
//	ChatClient chatClient(ChatClient.Builder builder) {
//		
//		// .defaultSystem(""): 빈문자열 에러
//		// - System Message
//		
//		return builder
//				.defaultSystem("당신은 SF영화 전문가입니다. 실력있는 전문가답게 영화에 대한 대답을 전문성 있고 간결하게 대답하세요.")
//				.build();
//	}
	
	@Value("classpath:/prompt.txt")
	private Resource prompt;
	
	// m2
//	@Bean
//	ChatClient chatClient(ChatClient.Builder builder) {
//		
//		//String txt = new String(prompt.getInputStream().readAllBytes());
//		
//		return builder
//				.defaultSystem(prompt)
//				.build();
//	}
	
	// m3
	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		
		return builder.build();
	}
	
	
}
