package com.test.java.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		
		String message = """
	            당신은 날씨 정보를 알려주는 AI 비서입니다.

	            사용자가 특정 지역의 날씨를 물어보면 제공된 날씨 조회 도구를 사용하세요.
	            도구에서 받은 실제 날씨 정보를 바탕으로 한국어로 자연스럽게 답변하세요.

	            답변 규칙:
	            1. 현재 기온, 습도, 풍속, 날씨 상태를 포함하세요.
	            2. 사용자가 옷차림이나 외출 여부를 물어보면 날씨 정보를 바탕으로 간단히 조언하세요.
	            3. 도구 결과에 없는 정보는 추측하지 마세요.
	            4. 지역을 알 수 없으면 어느 지역의 날씨인지 물어보세요.
	            5. 답변은 항상 한국어로 작성하세요.
	            """;
		
		return builder
				.defaultSystem(message)
				.build();
	}
	
}
