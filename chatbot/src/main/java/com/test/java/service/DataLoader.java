package com.test.java.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader {

	private final VectorStore vectorStore; 	// 등록
	private final JdbcClient jdbcClient;	// 제거
	
	@Value("classpath:호텔 매뉴얼.txt")
	private Resource manual; 
	
	public void load() {
		
		TikaDocumentReader reader = new TikaDocumentReader(manual);
		List<Document> documents = reader.get();
		
		String filename = manual.getFilename();
		List<Document> splitDocs = new ArrayList<>();
		
		// 줄단위로 분할
		for(Document doc : documents) {
			
			// 문단
			String fullContent = doc.getFormattedContent();
			
			String[] rules = fullContent.split("\\r\\n");
			
			for(String rule : rules) {
				
				if(rule.trim().isEmpty()) continue;
				
				Document ruleDoc = new Document(rule.trim());
				ruleDoc.getMetadata().put("file_name", filename);
				
				splitDocs.add(ruleDoc);
			}
			
		}
		
		// PGVector > 저장
		if(!splitDocs.isEmpty()) {
			vectorStore.accept(splitDocs);
			
			System.out.println("임베딩 완료: " + splitDocs.size());
			
		}
		
	}

	public void delete() {
		
		String filename = manual.getFilename();
		
		String sql = "delete from vector_store where metadata ->> 'file_name' = ?";
		
		int count = jdbcClient.sql(sql).param(filename).update();
		
		System.out.println("매뉴얼 삭제: " + count);
		
	}

}
