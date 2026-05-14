package com.test.java.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {
	
	//ChatModel > ChatClient
	//ImageModel
	
	//private final ChatClient chatClient;
	
	private final ImageModel imageModel;

	public ImageResponse generate(String message) {
		
		/*
		
			DALL-E 3 기준
			- 이미지 생성은 프롬프트 토큰 비용이 발생하지 않는다.
			- 이미지 1장당 > 생성 비용 발생
				- Standard: 장당 $0.04
				- HD: 장당 $0.08
			- 사이즈
				- 1024 x 1024
				- 1024 x 1792
				- 1792 x 1024
			- 다운로드 횟수는 무제한 + 비용 발생 안함.
			- 생성된 이미지 URL 제공 > 60분 후 제거
			
			GTP-4o Image API
			- DALL-E 3 보다 고비용
				
		*/
		
		return imageModel.call(
				new ImagePrompt(
					message, //사용자 입력 프롬프트
					ImageOptionsBuilder.builder() //옵션들
						//.model("")
						.N(1)
						.width(1024)
						.height(1024)
						.build()
				)				
		);
	}

}











