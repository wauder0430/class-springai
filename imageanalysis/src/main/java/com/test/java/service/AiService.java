package com.test.java.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final ChatClient chatClient;
	
	@Value("${upload.path}")
	private String path;

	public String analyse(MultipartFile imgFile) {
		
		try {
			
			//업로드 된 파일의 경로
			String fullPath = path + "\\" + imgFile.getOriginalFilename();
			
			Resource imgResource = new FileSystemResource(fullPath);
			
			String rawContentType = imgFile.getContentType();
			
			String finalContentType = (rawContentType != null) ? rawContentType : MimeTypeUtils.IMAGE_PNG_VALUE;
			
			return chatClient.prompt()
						.user(u -> u
							.text("첨부한 이미지를 분석해서, 자세한 설명을 텍스트로 만들어주세요.")
							.media(MimeTypeUtils.parseMimeType(finalContentType)
									, imgResource)
						)
						.call()
						.content();			
			
		} catch (Exception e) {
			System.out.println("AiService.analyse");
			e.printStackTrace();
		}

		
		return null;
	}
	
	
	
}











