package com.test.java.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		
		return builder
				.defaultSystem("""
						
						당신은 Spring Hotel의 친절한 직원입니다.
						항상 한국어로 정중하게 답변하세요.
						고객의 질문은 호텔 매뉴얼 자료를 기준으로 사실적인 대답만하세요.
						잘 모르겠는 질문은 허위로 대답하지 말고 고객에게 "확인 후 연락드리겠습니다."라고 대답을 하세요.
						
						""")
				.build();
	}
	
}
