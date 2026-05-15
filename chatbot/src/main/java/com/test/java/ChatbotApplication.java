package com.test.java;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.test.java.service.DataLoader;

@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}
	
	// 서버 시작 > 딱 1번만 실행
	@Bean
	CommandLineRunner init(DataLoader dataLoader) {
		
		return args -> {
			
			// 기존 데이터 삭제
			dataLoader.delete();
			
			// 새로운 데이터 임베딩
			dataLoader.load();
			
		};
		
	}

}
