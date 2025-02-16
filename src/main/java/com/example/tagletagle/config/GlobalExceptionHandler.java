package com.example.tagletagle.config;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse<BaseResponseStatus>> methodValidException(MethodArgumentNotValidException e){
		BindingResult bindingResult = e.getBindingResult();

		String errorMessage = null;

		FieldError fieldError = bindingResult.getFieldErrors().get(0);

		errorMessage = fieldError.getDefaultMessage();

		log.warn("MethodArgumentNotValidException has occured. please check request body. message: [{}]", e.getMessage());
		return ResponseEntity
			.status(400)
			.body(new BaseResponse(BaseResponseStatus.PARAMETER_ERROR, errorMessage));


	}

}
