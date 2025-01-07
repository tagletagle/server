package com.example.tagletagle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.tagletagle.jwt.JWTFilter;
import com.example.tagletagle.jwt.JWTUtil;
import com.example.tagletagle.oauth.handler.CustomSuccessHandler;
import com.example.tagletagle.oauth.service.CustomOauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOauth2UserService customOauth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JWTUtil jwtUtil;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//csrf disable : jwt로 stateless로 관리하기 때문에 csrf 설정을 해제
		http
			.csrf((auth) -> auth.disable());

		//Form 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//HTTP Basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		//JWTFilter 추가
		http
			.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		//oauth2
		http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOauth2UserService)))
				.successHandler(customSuccessHandler)
			);

		//경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/").permitAll()
				.anyRequest().permitAll());

		//세션 설정 : STATELESS
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}



}
