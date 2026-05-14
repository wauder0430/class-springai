package com.test.java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.test.java.entity.ImageAnalysis;
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
	
	@Value("${upload.path}")
	private String path;
	
	@PostMapping(value = "/analyze")
	public ResponseEntity<ImageAnalysis> analyze(
			@RequestParam("attach") MultipartFile imgFile
			) throws IOException {
		
		//파일 업로드 처리
		String filename = imgFile.getOriginalFilename();
		Path filePath = Paths.get(path, filename);
		Files.write(filePath, imgFile.getBytes());
		
		//AI에게 이미지 분석 요청
		String text = aiService.analyse(imgFile);
		
		//- http://localhost:8080/uploads/dog.png
		String imgUrl = "/uploads/" + filename;
		
		ImageAnalysis response = new ImageAnalysis(imgUrl, text);
		
		return ResponseEntity.ok(response);		
	}
	
}








