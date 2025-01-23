package com.example.tagletagle.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.oauth.dto.CustomOAuth2User;
import com.example.tagletagle.oauth.dto.OAuth2UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SocialJWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public SocialJWTFilter(JWTUtil jwtUtil) {

		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {

		//jwt 토큰 만료 -> 소셜 로그인 시도 ->jwt 토큰 만료.., 의 무한루프를 해결하기 위한 코드
		String requestUri = request.getRequestURI();
/*		if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}*/
		if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}


		//cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
		String authorization = null;
		Cookie[] cookies = request.getCookies();

		//토큰이 없는 경우
		if (cookies == null) {
			System.out.println("token null");
			filterChain.doFilter(request, response);

			//handleException(response, new BaseException(BaseResponseStatus.NO_EXIST_TOKEN));
			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("Authorization")) {

				authorization = cookie.getValue();
			}
		}

		//Authorization 헤더 검증
		if (authorization == null) {
			System.out.println("token null");
			filterChain.doFilter(request, response);

			//handleException(response, new BaseException(BaseResponseStatus.NO_EXIST_TOKEN));
			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//토큰
		String token = authorization;

		//토큰 소멸 시간 검증 + 토큰 유효성 검사
		try {
			if (jwtUtil.isExpired(token)) {

				System.out.println("token expired");
				//filterChain.doFilter(request, response);
				handleException(response, new BaseException(BaseResponseStatus.EXPIRED_TOKEN));
				//조건이 해당되면 메소드 종료 (필수)
				return;
			}
		}catch (io.jsonwebtoken.security.SignatureException e){
			handleException(response, new BaseException(BaseResponseStatus.INVALID_TOKEN));
			return;
		}catch (io.jsonwebtoken.MalformedJwtException e){
			handleException(response, new BaseException(BaseResponseStatus.INVALID_TOKEN));
			return;
		}catch (io.jsonwebtoken.ExpiredJwtException e){
			System.out.println("token expired");
			//filterChain.doFilter(request, response);
			handleException(response, new BaseException(BaseResponseStatus.EXPIRED_TOKEN));
			//조건이 해당되면 메소드 종료 (필수)
			return;

		}


		//토큰에서 username과 role 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		Long userId = jwtUtil.getUserId(token);

		//userDTO를 생성하여 값 set
		OAuth2UserDTO oAuth2UserDTO = new OAuth2UserDTO();
		oAuth2UserDTO.setUsername(username);
		oAuth2UserDTO.setRole(role);
		oAuth2UserDTO.setUserId(userId);

		//UserDetails에 회원 정보 객체 담기
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2UserDTO);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
		//세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

	private void handleException(HttpServletResponse response, BaseException e) throws IOException {
		// HTTP 상태 코드 설정
		response.setStatus(HttpServletResponse.SC_OK); // 또는 e.getStatusCode()
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// BaseResponse 형태의 에러 응답 작성
		BaseResponse<BaseResponseStatus> errorResponse = new BaseResponse<>(e.getStatus());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);

		// 응답 출력
		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}

}

