package com.example.tagletagle.src.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.utils.SecurityUtil;

@RestController
public class AuthController {
/*
	@GetMapping("/oauth2/authorization/naver")
	public String practiceNaver(){
		return "성공";
	}

	@GetMapping("/oauth2/authorization/google")
	public String practiceGoogle(){
		return "성공";
	}*/

	@GetMapping("/social/login")
	public Long testSocialLogin(){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			return userId;
		}catch (BaseException e){
			return -1L;
		}
	}

	@GetMapping("/social")
	public String testSecurityConfig(){
		return "성공 security";
	}


}
