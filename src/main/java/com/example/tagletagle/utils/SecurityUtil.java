package com.example.tagletagle.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.tagletagle.auth.CustomUserDetails;
import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.oauth.dto.CustomOAuth2User;

public class SecurityUtil {

	//userId 가져오기
	public static Optional<Long> getCurrentUserId(){
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomOAuth2User customOAuth2User;
		CustomUserDetails customUserDetails;

		Object principal = authentication.getPrincipal();
		if(principal instanceof CustomOAuth2User){
			try {
				customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
			}catch (ClassCastException e){
				return Optional.empty();
			}catch (Exception e){
				throw new BaseException(BaseResponseStatus.UNEXPECTED_ERROR);
			}

			return Optional.of(customOAuth2User.getUserId());
		}
		else if(principal instanceof CustomUserDetails){
			try {
				customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			}catch (ClassCastException e){
				return Optional.empty();
			}catch (Exception e){
				throw new BaseException(BaseResponseStatus.UNEXPECTED_ERROR);
			}

			return Optional.of(customUserDetails.getUserId());

		}
		else{
			throw new BaseException(BaseResponseStatus.USER_NO_EXIST);
		}



	}

	//role 가져오기
	public static String getCurrentUserRole(){

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();

		return auth.getAuthority();


	}

}
