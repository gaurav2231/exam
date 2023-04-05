package com.questionbank.model;

import lombok.Data;

@Data
public class ResponseBody {

	private int statusCode;

	private String message;

	public ResponseBody(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

}