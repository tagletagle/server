package com.example.tagletagle.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse<T> {

	//JsonPropertyOrder로 순서 정하기
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	private boolean isSuccess;
	private String message;
	int code;

	public BaseResponse(T data) {
		this.data = data;
		this.code = BaseResponseStatus.Success.getCode();
		this.isSuccess = BaseResponseStatus.Success.isSuccess();
		this.message = BaseResponseStatus.Success.getMessage();
	}

	public BaseResponse(BaseResponseStatus status) {
		this.data = null;
		this.code = status.getCode();
		this.isSuccess = status.isSuccess();
		this.message = status.getMessage();

	}

	public BaseResponse(BaseResponseStatus status, String errorMessage) {
		this.data = null;
		this.code = status.getCode();
		this.isSuccess = status.isSuccess();
		this.message = errorMessage;
	}

}
