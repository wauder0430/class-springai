package com.test.java.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader {
	
	private final VectorStore vectorStore;	// 벡터 DB 조작 (저장 검색)
	private final JdbcClient jdbcClient; 	// SQL을 직접 실행
	
	@Value("classpath:SPRi AI Brief_12월호_산업동향_1208_F.pdf")
	private Resource pdfResource;

	public void load() {
		
		/*
			RAG 처리
			1. PDF > 읽기
			2. 내용을 조각내기 == 청킹(Chunking) > 조각난 문자열 == 청크(Chunk)
				- 문단
				- 문장
				- 일정 글자 수(100~500)
				
			3. 청크를 벡터화 
				- 문장 > 토큰 > 벡터화
				- ex) 	강아지 > 1
					 	멍멍이 > 1.1
					 	고양이 > 3
			4. DB에 저장
				- Vector Database
				- PGVector
				
			
			1. LLM에게 질문
			2. 질문을 벡터화
			3. Vector > 검색 > 질문과 유사한 청크들을 가져오기
			4. 1번 원래 질문 + 청크들 > LLM에게 질문
		
		*/
		
		// 임베딩: 문서 > 청킹 > 벡터 > 벡터DB
		
		// 문서 로딩
		TikaDocumentReader reader = new TikaDocumentReader(pdfResource);
		List<Document> documents = reader.get();
		
		// 문서 > 메타 데이터 추가(삭제용)
		String fileName = pdfResource.getFilename(); // 파일명
		documents.forEach(doc -> doc.getMetadata().put("filename", fileName));
		
		// 청킹
		TokenTextSplitter splitter = new TokenTextSplitter();
		
		List<Document> splitDocs = splitter.apply(documents);
		
		// 저장
		vectorStore.accept(splitDocs);
		
		System.out.println("임베딩 된 데이터 수: " + splitDocs.size());
		
	}

	public void del() {

		String filename = pdfResource.getFilename();
		
		String sql = "delete from vector_store where metadata ->> 'filename' = ?";
		
		int count = jdbcClient.sql(sql).param(filename).update();
		
		System.out.println("삭제된 데이터 수: " + count);	
		
	}

	
	
}
