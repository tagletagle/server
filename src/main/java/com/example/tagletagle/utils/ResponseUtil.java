package com.example.tagletagle.utils;

import java.io.IOException;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtil {

	public static void handleResponse(HttpServletResponse response, Object object) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK); // 또는 e.getStatusCode()
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// BaseResponse 형태의 에러 응답 작성
		BaseResponse<Object> baseResponse = new BaseResponse<>(object);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(baseResponse);

		// 응답 출력
		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}

	public static void handleException(HttpServletResponse response, int status , BaseException e) {

		try{
			// HTTP 상태 코드 설정
			response.setStatus(status); // 또는 e.getStatusCode()
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// BaseResponse 형태의 에러 응답 작성
			BaseResponse<BaseResponseStatus> errorResponse = new BaseResponse<>(e.getStatus());
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			// 응답 출력
			response.getWriter().write(jsonResponse);
			response.getWriter().flush();
		}catch (IOException ioException){
			throw new RuntimeException(ioException);
		}

	}



}
