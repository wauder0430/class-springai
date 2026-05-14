package com.test.java.service;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {
	
	private final ChatClient chatClient;
	private final JdbcClient jdbcClient;
	private final VectorStore vectorStore;

	public List<String> files() {
		
		String sql = """
		
			select distinct metadata ->> 'file_name'
			from vector_store
			where metadata ->> 'file_name' is not null		
				
		""";
		
		return jdbcClient.sql(sql).query(String.class).list();
	}

	public void embed(MultipartFile attach) throws IOException {
		
		String filename = attach.getOriginalFilename();
		
		ByteArrayResource resource = new ByteArrayResource(attach.getBytes());
		TikaDocumentReader reader = new TikaDocumentReader(resource);
 		List<Document> documents = reader.get();
 		
 		documents.forEach(doc -> doc.getMetadata().put("file_name", filename));
 		
 		TokenTextSplitter splitter = new TokenTextSplitter();
 		
 		List<Document> splitDocs = splitter.apply(documents);
 		
 		vectorStore.accept(splitDocs);
		
	}

}








