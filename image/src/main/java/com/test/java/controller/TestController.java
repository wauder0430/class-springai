package com.test.java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {

	private final AiService aiService;
	
	@GetMapping(value = "/")
	public String index(Model model) {

		return "index";
	}
	
	@PostMapping(value = "/")
	public String indexok(Model model
			, @RequestParam("message") String message) {

		ImageResponse resp = aiService.generate(message);
		
		//
		//System.out.println(resp);
		
		//<img>에 출력
		String img = resp.getResult().getOutput().getB64Json();
		model.addAttribute("img", img);
		
		
		//
		byte[] imageBytes = Base64.getDecoder().decode(img);
		
		try {
			Files.write(Path.of("C:\\code\\springai\\dog.png"), imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "index";
	}
	
}

















