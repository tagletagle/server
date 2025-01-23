package com.example.tagletagle.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.tagletagle.auth.LoginFilter;
import com.example.tagletagle.jwt.JWTFilter;
import com.example.tagletagle.jwt.SocialJWTFilter;
import com.example.tagletagle.jwt.JWTUtil;
import com.example.tagletagle.oauth.handler.CustomSuccessHandler;
import com.example.tagletagle.oauth.service.CustomOauth2UserService;
import com.example.tagletagle.src.user.repository.RefreshRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOauth2UserService customOauth2UserService;
	private final CustomSuccessHandler customSuccessHandler;

	private final AuthenticationConfiguration authenticationConfiguration;

	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

					CorsConfiguration configuration = new CorsConfiguration();

					configuration.setAllowedOrigins(Collections.singletonList("*"));
					configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
					configuration.setAllowCredentials(true);
					configuration.setAllowedHeaders(Collections.singletonList("*"));
					configuration.setMaxAge(3600L);

					configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
					configuration.setExposedHeaders(Collections.singletonList("Authorization"));

					return configuration;
				}
			}));

		//csrf disable : jwt로 stateless로 관리하기 때문에 csrf 설정을 해제
		http
			.csrf((auth) -> auth.disable());

		//Form 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//HTTP Basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		http
			.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

		//JWTFilter 추가
		/*http
				.addFilterBefore(new SocialJWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);*/
			//.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

		//oauth2
		/*http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOauth2UserService)))
				.successHandler(customSuccessHandler)
			);

		http.
			oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/login")
		);*/

		//경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/login", "/reissue").permitAll()
				.requestMatchers("/social/login").authenticated()
				.anyRequest().permitAll());

		// logout 필터 비활성화
		http
			.logout(logout -> logout.disable());

		//세션 설정 : STATELESS
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}



}
