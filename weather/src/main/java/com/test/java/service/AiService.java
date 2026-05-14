package com.test.java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.test.java.tool.ClothTool;
import com.test.java.tool.WeatherTool;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final ChatClient chatClient;
	private final WeatherTool tool;
	private final ClothTool ctool;

	public String weather(String message) {
		
		/*
		 	***** Tool Calling 처리 순서
		 	
		 	1. 지금 서울인데 오늘 우산을 가지고 외출할까?
		 	2. 1을 LLM 요청
		 	3. LLM이 질문 판단 > 날씨?
		  	4. 날씨 조회 도구 인식 > Spring > 도구 호출
		  	5. 알아낸 날씨 > LLM 
		  	6. 최종 응답
		  
		*/
		
		return chatClient.prompt()
					.user(message)
					.tools(tool, ctool) // 툴 실행(X), 툴 등록(O)
					.call()
					.content();
	}
	
}
