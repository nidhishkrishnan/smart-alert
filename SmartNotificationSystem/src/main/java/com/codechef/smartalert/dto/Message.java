package com.codechef.smartalert.dto;

import org.springframework.stereotype.Component;

@Component
public class Message {
	public String message;
	public Object data;
	public String apiKey;

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
