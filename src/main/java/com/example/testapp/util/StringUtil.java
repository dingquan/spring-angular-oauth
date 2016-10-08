package com.example.testapp.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtil {

	private static ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
	}
	
	/** replace null with empty string */
	public static String nullSafeString(String str) {
		return str != null ? str : "";
	}
	
	public static String toJson(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
	
	public static String toJsonPretty(Object o) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
}
