package com.test.java.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ClothTool {

	// 내가 가진 옷들 중 > 날씨에 따라 어떤 옷을 입을지?
	// - 맑은 > A사 티셔츠
	// - 흐린 > B사 점퍼
	// - ql > C사 우의
	
	@Tool(description = """
    	    날씨에 따라 입고 나갈 의상을 결정합니다.
    	    정해진 날씨(맑음, 비, 눈 등)을 전달하면 입고 나갈 옷을 문자열로 반환합니다.
			
			예) 맑음 > A사 티셔츠
    	    	비 > B사 우의
    	    	
    	    만약 정해진 날씨가 아니면.. 의상을 사용자가 자유롭게 선택하도록 답변해라.
    	    """)
	public String getCurrentCloth(String weather) {
		
		if (weather.equals("맑음"))
			return "A사 티셔츠";
		else if (weather.equals("비"))
			return "B사 우의";
		else if (weather.equals("눈"))
			return "C사 오리털 파카";
		else if (weather.equals("소나기"))
			return "A사 반팔 라운드 티";
		else if (weather.equals("뇌우"))
			return "B사 정장";
			
		return "";
	}
	
	
}
