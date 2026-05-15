package com.test.java.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {

	private final AiService aiService;
	
	@GetMapping(value = "/")
	public String index() {

		return "index";
	}
	
	@GetMapping(value = "/files")
	public @ResponseBody ResponseEntity<List<String>> files() {
		
		return ResponseEntity.ok(aiService.files());
	}
	
	@PostMapping(value = "/upload")
	public @ResponseBody ResponseEntity<String> upload(@RequestParam("attach") MultipartFile attach) {

		try {
			aiService.embed(attach);
			return ResponseEntity.ok("등록 완료");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("등록 실패");
		}

	}
	
	@PostMapping(value = "/del")
	public @ResponseBody ResponseEntity<String> del(@RequestParam("filename") String filename) {

		try {
			aiService.del(filename);
			return ResponseEntity.ok("삭제 완료");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("삭제 실패");
		}

	}
	
	@PostMapping(value = "/chat")
	public @ResponseBody ResponseEntity<String> chat(@RequestParam("message") String message) {
		
		String result = aiService.chat(message);
		
		return ResponseEntity.ok(result);
	}
	
	
}








