package com.test.java.service;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AiService {

	//private final OpenAiAudioTranscriptionModel audioModel;
	private final OpenAiAudioSpeechModel audioModel;
	
	public Flux<byte[]> streamAudio(String content) {
		
		//Spring AI 1.1.1 부터 > 프롬프트 작성 없이
		return audioModel.stream(content);
	}
	
}





