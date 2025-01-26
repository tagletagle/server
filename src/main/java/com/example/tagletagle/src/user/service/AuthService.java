package com.example.tagletagle.src.user.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.jwt.JWTUtil;
import com.example.tagletagle.src.user.dto.RefreshTokenRequestDTO;
import com.example.tagletagle.src.user.dto.TokenResponseDTO;
import com.example.tagletagle.src.user.entity.RefreshEntity;
import com.example.tagletagle.src.user.repository.RefreshRepository;
import com.example.tagletagle.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;
	public TokenResponseDTO reissue(RefreshTokenRequestDTO refreshTokenRequestDTO) {

		// get refresh token
		String refreshToken = refreshTokenRequestDTO.getRefreshToken();

		//Authorization 헤더 검증
		if (!refreshToken.startsWith("Bearer ")) {

			throw new BaseException(BaseResponseStatus.NO_BEARER_FORM);
		}

		//Bearer 부분 제거 후 순수 토큰만 획득
		String refresh = refreshToken.split(" ")[1];

		// 토큰 유효성 검사
		try{
			jwtUtil.isExpired(refresh);
		}catch (io.jsonwebtoken.security.SignatureException e){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}catch (io.jsonwebtoken.MalformedJwtException e){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}catch (io.jsonwebtoken.ExpiredJwtException e){
			throw new BaseException(BaseResponseStatus.EXPIRED_REFRESH_TOKEN);
		}

		// 토큰이 refresh인지 검사
		String category = jwtUtil.getCategory(refresh);

		if(!category.equals("refresh")){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}

		// DB에 refresh token이 있는지 검사
		Boolean isExist = refreshRepository.existsByRefresh(refresh);
		if(!isExist){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}

		String username = jwtUtil.getUsername(refresh);
		Long userId = jwtUtil.getUserId(refresh);
		String role = jwtUtil.getRole(refresh);
		// 1일
		String newAccess = jwtUtil.createJwt("access", username,userId, role, 24*60*60*1000L);
		// 7일
		String newRefresh = jwtUtil.createJwt("refresh", username, userId, role, 7*24*60*60*1000L);

		// DB에 기존 refresh 토큰 삭제 후, 새 refresh 토큰 저장
		refreshRepository.deleteByRefresh(refresh);
		addRefreshEntity(username, newRefresh, 7*24*60*60*1000L);

		return new TokenResponseDTO("Bearer " + newAccess, "Bearer " + newRefresh);

	}

	private void addRefreshEntity(String username, String refresh, Long expiredMs) {

		Date date = new Date(System.currentTimeMillis() + expiredMs);

		RefreshEntity refreshEntity = new RefreshEntity();
		refreshEntity.setUsername(username);
		refreshEntity.setRefresh(refresh);
		refreshEntity.setExpiration(date.toString());

		refreshRepository.save(refreshEntity);
	}

	public void logout(RefreshTokenRequestDTO refreshTokenRequestDTO) {

		// get refresh token
		String refreshToken = refreshTokenRequestDTO.getRefreshToken();

		//Authorization 헤더 검증
		if (!refreshToken.startsWith("Bearer ")) {

			throw new BaseException(BaseResponseStatus.NO_BEARER_FORM);
		}

		//Bearer 부분 제거 후 순수 토큰만 획득
		String refresh = refreshToken.split(" ")[1];

		// 토큰 유효성 검사
		try{
			jwtUtil.isExpired(refresh);
		}catch (io.jsonwebtoken.security.SignatureException e){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}catch (io.jsonwebtoken.MalformedJwtException e){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}catch (io.jsonwebtoken.ExpiredJwtException e){
			throw new BaseException(BaseResponseStatus.EXPIRED_REFRESH_TOKEN);
		}

		// 토큰이 refresh인지 검사
		String category = jwtUtil.getCategory(refresh);

		if(!category.equals("refresh")){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}

		// DB에 refresh token이 있는지 검사
		Boolean isExist = refreshRepository.existsByRefresh(refresh);
		if(!isExist){
			throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
		}

		// 로그아웃 진행
		refreshRepository.deleteByRefresh(refresh);


	}

}
