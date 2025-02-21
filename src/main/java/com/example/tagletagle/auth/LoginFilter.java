package com.example.tagletagle.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.jwt.JWTUtil;
import com.example.tagletagle.src.user.dto.LoginDTO;
import com.example.tagletagle.src.user.dto.LoginResponseDTO;
import com.example.tagletagle.src.user.entity.RefreshEntity;
import com.example.tagletagle.src.user.repository.RefreshRepository;
import com.example.tagletagle.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		//ObjectMapper 이용 시, json 형식으로 로그인 요청을 받을 수 있다.(username, password)
		ObjectMapper om = new ObjectMapper();
		LoginDTO loginDTO = null;

		try {
			loginDTO = om.readValue(request.getInputStream(), LoginDTO.class);
		} catch (IOException e) {
			//throw new RuntimeException(e);

			System.out.println(e.getMessage());

			ResponseUtil.handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR ,new BaseException(
				BaseResponseStatus.NO_VALID_LOGINDTO));
			return null;
		}

		if(loginDTO == null){
			ResponseUtil.handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR ,new BaseException(
				BaseResponseStatus.NO_VALID_LOGINDTO));

			return null;
		}

		if(loginDTO.getUsername() == null || loginDTO.getUsername().isBlank()){
			ResponseUtil.handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR ,new BaseException(
				BaseResponseStatus.NO_VALID_LOGINDTO));

			return null;
		}

		String username = loginDTO.getUsername();
		//String username = "눈사람";
		String password = "tagle1234";

		System.out.println(username + " " + password);
		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

		//token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}

	//로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException{

		//UserDetailsS
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String role = auth.getAuthority();
		Long userId = customUserDetails.getUserId();

		// 1일
		// String accessToken = jwtUtil.createJwt("access", username, userId, role, 24*60*60*1000L);
		String accessToken = jwtUtil.createJwt("access", username, userId, role, 10*60*1000L);

		// 7일
		// String refreshToken = jwtUtil.createJwt("refresh", username, userId, role, 7*24*60*60*1000L );
		String refreshToken = jwtUtil.createJwt("refresh", username, userId, role, 20*60*1000L );

		//addRefreshEntity(username, refreshToken, 7*24*60*60*1000L);
		addRefreshEntity(username, refreshToken, 20*60*1000L);


		response.addHeader("access", "Bearer " + accessToken);
		response.addHeader("refresh", "Bearer " + refreshToken);
		ResponseUtil.handleResponse(response, new LoginResponseDTO(customUserDetails.getIsNewUser(), "Bearer "+ accessToken, "Bearer " + refreshToken));

	}

	//로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

		System.out.println("fail");
		ResponseUtil.handleException(response,HttpServletResponse.SC_UNAUTHORIZED ,new BaseException(BaseResponseStatus.FAILED_LOGIN));
	}

	private void addRefreshEntity(String username, String refresh, Long expiredMs) {

		Date date = new Date(System.currentTimeMillis() + expiredMs);

		RefreshEntity refreshEntity = new RefreshEntity();
		refreshEntity.setUsername(username);
		refreshEntity.setRefresh(refresh);
		refreshEntity.setExpiration(date.toString());

		refreshRepository.save(refreshEntity);
	}

}
