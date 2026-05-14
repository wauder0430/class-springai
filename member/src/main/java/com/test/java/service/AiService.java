package com.test.java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.test.java.tool.MemberTool;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final ChatClient chatClient;
	private final MemberTool memberTool;

	public String member(String message) {
		
		return chatClient.prompt()
					.user(message)
					.tools(memberTool)
					.call()
					.content();
	}
	
}
