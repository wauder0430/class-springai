package com.test.java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final ChatClient chatClient;
	private final VectorStore vectorStore;

	public String chat(String message) {
		
		//RAG 기반
		
		
		return chatClient.prompt()
				.user(message)
				.advisors(
					QuestionAnswerAdvisor.builder(vectorStore)
						.searchRequest(SearchRequest.builder().build())
						.build()
				)				
				.call()
				.content();
	}
	
}










