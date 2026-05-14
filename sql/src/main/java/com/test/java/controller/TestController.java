package com.test.java.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.java.entity.SqlResponse;
import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {

	private final AiService aiService;
	
	@GetMapping(value ="/")
	public String index(Model model) {
		
		return "index";
	}
	
	@PostMapping(value ="/sql")
	public @ResponseBody ResponseEntity<SqlResponse> sql(@RequestParam("text") String text) throws IOException {
		
		// System.out.println(text);
		SqlResponse resp = aiService.excute(text);
		
		//System.out.println(resp);
		
		return ResponseEntity.ok(resp);
	}
	
}
