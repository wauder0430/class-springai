package com.test.java.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	private final ChatClient chatClient;
	
	@GetMapping(value = "/")
	public String test(Model model, @RequestParam("message") String message) {
		
		// - /?message=질문
		
		return chatClient
					.prompt()	
					.user(message)	
					.call()
					.content();
	}
	
	
	/*
	@GetMapping("/")
	public String (Model model) {
		
		return "";
	}
	*/
	
	
}
