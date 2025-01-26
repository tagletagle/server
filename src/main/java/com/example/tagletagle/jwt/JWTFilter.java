package com.example.tagletagle.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tagletagle.auth.CustomUserDetails;
import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.utils.ResponseUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {

		this.jwtUtil = jwtUtil;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {

		//jwt 토큰 만료 -> 소셜 로그인 시도 ->jwt 토큰 만료.., 의 무한루프를 해결하기 위한 코드
		String requestUri = request.getRequestURI();
		if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}
		if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}

		System.out.println("authorization 시작~~~~~~~~~~~~~");

		String accessToken = null;

		try {
			//request에서 Authorization 헤더를 찾음
			accessToken = request.getHeader("Authorization");
		}catch (IllegalArgumentException e){
			ResponseUtil.handleException(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR ,new BaseException(BaseResponseStatus.HEADER_ERROR));
		}

		// token이 필요없는 요청
		if (accessToken == null) {

			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//Authorization 헤더 검증
		if (!accessToken.startsWith("Bearer ")) {

			ResponseUtil.handleException(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR ,new BaseException(BaseResponseStatus.NO_BEARER_FORM));

			//조건이 해당되면 메소드 종료 (필수)
			filterChain.doFilter(request, response);
			return;
		}


		System.out.println("authorization now");
		//Bearer 부분 제거 후 순수 토큰만 획득
		String token = accessToken.split(" ")[1];

		//토큰 소멸 시간 검증
		try {
			if (jwtUtil.isExpired(token)) {

				System.out.println("token expired");
				filterChain.doFilter(request, response);

				//조건이 해당되면 메소드 종료 (필수)
				return;
			}
		}catch (io.jsonwebtoken.security.SignatureException e){
			ResponseUtil.handleException(response,HttpServletResponse.SC_UNAUTHORIZED ,new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN));
			return;
		}catch (io.jsonwebtoken.MalformedJwtException e){
			ResponseUtil.handleException(response, HttpServletResponse.SC_UNAUTHORIZED, new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN));
			return;
		}catch (io.jsonwebtoken.ExpiredJwtException e){
			System.out.println("token expired");
			//filterChain.doFilter(request, response);
			ResponseUtil.handleException(response, HttpServletResponse.SC_UNAUTHORIZED ,new BaseException(BaseResponseStatus.EXPIRED_ACCESS_TOKEN));
			//조건이 해당되면 메소드 종료 (필수)
			return;

		}

		String category = jwtUtil.getCategory(token);
		if(!category.equals("access")){
			ResponseUtil.handleException(response, HttpServletResponse.SC_UNAUTHORIZED, new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN));
			return;
		}

		//토큰에서 username과 role 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		Long userId = jwtUtil.getUserId(token);

		//userEntity를 생성하여 값 set
		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(username);
		userEntity.setPassword("temppassword");
		userEntity.setRole(role);

		//UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(userEntity, userId);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		//세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
