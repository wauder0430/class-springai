package com.test.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String index(Model model) {

		return "index";
	}
	
	@PostMapping(value = "/transcribe")
	public @ResponseBody ResponseEntity<String> transcribe(Model model, @RequestParam("attach") MultipartFile attach) {
		
		String result = aiService.transcribe(attach);
		
		//넷플릭스는 글로벌 진출 후 오리지널 시리즈 영화 제작과 콘텐츠 라인 선싱에 총 1350억 달러 약 200조 원을 투자해 약 3250억 달러 약 483조 원의 경제적 효과를 창출했다고 12일 현지시간 공개했다. 넷플릭스 오리지널은 직접 제작 공동투자 라이선스 취득 등의 방식 으로 배급하는 독점 콘텐츠를 말한다. 넷플릭스는 그간 50개 이상의 나라에서 50개의 언어로 오리지널 콘텐츠 를 제작했다.
		



		System.out.println(result);

		return ResponseEntity.ok(result);
	}
	
}





