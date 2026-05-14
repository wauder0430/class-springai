package com.test.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {

	private final AiService aiService;
	
	@GetMapping(value = "/")
	public String index(Model model) {
		
		return  "index";
		
	}
	
	@PostMapping(value = "/weather")
	public @ResponseBody ResponseEntity<String> weather(@RequestParam("message") String message) {
		
		String result = aiService.weather(message);
		
		return ResponseEntity.ok(result);
		
	}
	
}
