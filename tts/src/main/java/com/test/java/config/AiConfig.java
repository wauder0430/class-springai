package com.test.java.config;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
	
	@Value("${spring.ai.openai.api-key}")
	private String apiKey;
	
	@Bean
	OpenAiAudioTranscriptionModel audio() {
		
		OpenAiAudioApi api = OpenAiAudioApi.builder()
								.apiKey(apiKey)
								.build();
		
		return new OpenAiAudioTranscriptionModel(api);
	}
	
}

