package com.test.java.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.test.java.service.AiService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class TestController {
	
	private final AiService aiService;
	
	@GetMapping(value = "/")
	public String index(Model model) {

		return "index";
	}
	
	@PostMapping(value = "/upload")
	public ResponseEntity<StreamingResponseBody> upload(Model model, @RequestParam("attach") MultipartFile attach) throws IOException {
		
		//텍스트 파일 > 텍스트 추출
		String content = new String(attach.getBytes(), StandardCharsets.UTF_8);
		
		
		//AI에게 문장을 읽어달라고 요청
		//1. 완료 후 응답 > 전부 음성 바꾼 뒤 한번에 돌려주는 방식
		//2. 실시간 응답 > 우선 번역한 일부 반환 + 이어서 계속 번역 > 반복
 		Flux<byte[]> responseStream = aiService.streamAudio(content);
		
 		//3. 스트리밍 응답 생성
 		StreamingResponseBody stream = outputStream -> responseStream.toStream().forEach(bytes -> writeToOutput(outputStream, bytes));
 		
 		//4. 응답 처리
 		
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=speech.mp3")
                .body(stream);
	}

	private Object writeToOutput(OutputStream outputStream, byte[] bytes) {
		
		try {
			if (bytes != null && bytes.length > 0) {
				outputStream.write(bytes); //bytes(음성) > 브라우저에게 반환
				outputStream.flush();
			}
		} catch (Exception e) {
			System.out.println("TestController.writeToOutput");
			e.printStackTrace();
		}

		
		return null;
	}

}






