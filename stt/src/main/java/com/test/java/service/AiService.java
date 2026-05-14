package com.test.java.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {
	
	private final OpenAiAudioTranscriptionModel audioModel;

	public String transcribe(MultipartFile attach) {
		
		try {
			
			//0: 가장 확실한 단어만 선택. 토시 하나 안틀리고 그대로 적으로 노력. 회의록
			//1: 불명확한 음성 > LLM 마음대로 의역.. 대화, 브레인스토밍..
			OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
					.responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
					.language("ko") //음성 > 한국어 인식
					.temperature(0F)
					.build();
			
			//프롬프트 생성
			AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(attach.getResource(), options);
			
			//AI 모델 호출
			AudioTranscriptionResponse resp = audioModel.call(prompt);
			
			//결과값 반환
			return resp.getResult().getOutput();
			
		} catch (Exception e) {
			System.out.println("AiService.transcribe");
			e.printStackTrace();
		}

		
		return null;
	}

}





