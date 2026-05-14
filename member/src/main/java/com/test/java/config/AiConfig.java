package com.test.java.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		
		String message = """
	            당신은 회원 정보를 조회해주는 AI 비서입니다.

	            사용자가 회원 정보, 회원 상세 정보, 회원 활동 내역을 물어보면 제공된 회원 조회 도구를 사용하세요.
	            도구에서 조회한 실제 DB 결과를 바탕으로 한국어로 답변하세요. 절대 너 스스로 대답을 지어내지 마세요. (환각 금지!!!)

	            답변 규칙:
	            1. 회원 정보를 물어보면 회원 조회 도구를 사용하세요.
	            2. 게시글 수, 댓글 수, 최근 게시글을 물어보면 회원 활동 조회 도구를 사용하세요.
	            3. DB 조회 결과에 없는 내용은 추측하지 마세요.
	            4. 비밀번호는 절대 답변에 포함하지 마세요.
	            5. 회원이 존재하지 않으면 존재하지 않는 회원이라고 답변하세요.
	            6. 답변은 항상 한국어로 작성하세요.
	            """;
		
		return builder
				.defaultSystem(message)
				.build();
	}
	
}
