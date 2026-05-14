package com.test.java.entity;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SqlResponse {

	private String query;
	private List<Map<String, Object>> result;
	
}
