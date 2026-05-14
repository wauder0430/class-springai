package com.test.java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final ChatClient chatClient;

	public String m1(String message) {
		
		// 이쪽 System이 우선순위 높음
		return chatClient.prompt()
				.system("당신은 한국 영화 전문가")
				.user(message)
				.call()
				.content();
	}

	public String m2(String message, String subject, String wordCount) {

		return chatClient.prompt()
				.user(message)
				.system(sp-> sp.param("subject", subject).param("wordCount", wordCount))
				.call()
				.content();
	}

	public String m3(String food, String style) {
		/*
			system() = 설정, 규칙 (틀)
				- AI의 페르소나(인격), 답변 스타일 > 설정
			
			user() = 요청
				- 실제 처리해야할 요구 사항
				- 질문
		*/
		String txt = "'{food}' 음식에 대한 '{style}' 방식의 조리법을 간단하게 알려줘";
		
		return chatClient.prompt()
				.user(sp -> sp.text(txt)
								.param("food", food)
								.param("style", style))
				.call()
				.content();
	}
	
}
