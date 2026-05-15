package com.test.java.service;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
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

	public void del(String filename) {
		
		String sql = "delete from vector_store where metadata ->> 'file_name' = ?";
		jdbcClient.sql(sql).param(filename).update();
		
	}

	public String chat(String message) {

		// RAG 기반 검색
		
		// System Message 없음: LLM이 일반 지식을 섞어서 대답할 확률이 높다. 
		// System Message 있음: 문서 기반으로 대답할 확률이 높다.
		/*
			RAG 검색 옵션
			1. similarityThreshold
				- 유사도 
				- 코사인 유사도를 사용
			2. topK
				- 가져온 청크 중 유사도 높은 순으로 개한 제한
				- 기본값: 4
		
			topK: 가져올 청크의 개수
			similarityThreshold: 가져올 청크의 품질(유사도)
		
			similarityThreshold 크다.
			- 관련도가 아주 높은 것만 가져온다.
			- 답변이 깔끔하고 명확하다.
		 	- 문서를 못 찾을 가능성이 높다.
		 	
		 	similarityThreshold 낮다.
		 	- 문서를 더 쉽게 가져온다.
		 	- 답변이 잘나오지만 관련없는 대답 확률 높음
		 	
		 	topK 높다
		 	- 자료를 많이 가져온다.
		 	- 답변에 사용할 재료는 많아지지만, 관련 없는 내용도 섞일 수 있다.
		 	
		 	topK 낮다
		 	- 자료를 적게 가져온다.
		 	- 답변이 정확하고 깔끔해지지만, 필요한 문서가 빠질 수 있다.
		 	
		 	topK	similarityThreshold
		 	8		0						> 넉넉하게
		 	8		0.5						> 적당히 관련된
		 	7		0.6						> 조금 더 타이트
		 	5		0.6						> 조금 더 타이트
	
		*/
		
		return chatClient.prompt()
							.system("""
                        당신은 업로드된 문서를 기반으로 답변하는 RAG assistant입니다.

                        답변 규칙:
                        1. 반드시 제공된 참고 문서 내용에 근거해서만 답변하세요.
                        2. 참고 문서에 없는 내용은 추측하지 말고 "문서에서 확인할 수 없습니다."라고 답변하세요.
                        3. 일반 지식, 사전 지식, 인터넷 지식을 섞지 마세요.
                        4. 답변은 한국어로 작성하세요.
                        5. 가능한 경우 핵심 내용을 먼저 요약하고, 필요한 세부 내용을 뒤에 설명하세요.
									""")
							.user(u -> u.text("""
									사용자의 질문에 답변하세요.
									
									질문
									{question}
									""").param("question", message))
							.advisors(
								QuestionAnswerAdvisor.builder(vectorStore)
									.searchRequest(SearchRequest.builder()
													.similarityThreshold(0.2)
													.topK(5)
													.build())
									.build()
									)
							.call()
							.content();
		
	}

}


/*
	eu 회원국들이 ai 인재 유치 노력을 어떻게 하고 있니?
 
 	- EU 회원국들이 AI 인재 유치 노력을 하고 있지만, 그 노력은 여전히 제한적이며 주로 학계에 편중된 상황입니다. OECD의 보고서에 따르면, EU 회원국들은 AI 연구 강화와 디지털 리터러시 강화를 위한 정책을 추진하고 있으나, AI 인재 유치에 대한 구체적인 전략이나 실행 현황은 부족한 것으로 나타났습니다. 

따라서, AI 인재 유치에 대한 구체적인 노력이나 성과에 대한 정보는 문서에서 확인할 수 없습니다.

	-EU 회원국들은 AI 인재 유치를 위해 다양한 노력을 하고 있지만, 주로 학계에 편중된 상황입니다. OECD 보고서에 따르면, 거의 모든 EU 회원국이 AI 조정계획에 따라 AI 전략을 채택하고 있으나, AI 인재 유치 노력은 제한적입니다. 

AI 연구와 교육을 강화하기 위한 정책이 추진되고 있지만, 중소기업들은 AI 도입의 저해 요인으로 역량 부족을 지적하고 있습니다. 또한, AI 전문 교육 프로그램은 아직 초기 단계이며 점진적으로 증가하는 추세입니다. 약 4분의 1의 EU 회원국이 장학금이나 보조금을 통해 AI 연구자와 대학원생을 확보하는 제도를 운영하고 있지만, 민간 부문의 인재 수요와 괴리가 존재합니다.

결론적으로, EU 회원국들은 AI 인재 유치에 대한 노력을 하고 있으나, 그 효과는 제한적이며 학계 중심으로 이루어지고 있습니다.
 
 
*/





