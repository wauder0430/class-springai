package com.test.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;


@Controller
@RequiredArgsConstructor
public class TestController {

	private final AiService aiService;
	
	@GetMapping(value = "/")
	public String index() {
		return "index";
	}
	
	@PostMapping(value = "/chat")
	public @ResponseBody Flux<String> chat(@RequestParam("message") String message) {

		Flux<String> result = aiService.chat(message);
		
		return result;
	}
	
	
}
