package com.example.tagletagle.oauth.handler;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.tagletagle.jwt.JWTUtil;
import com.example.tagletagle.oauth.dto.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JWTUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		//OAuth2User
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

		String username = customUserDetails.getUsername();
		Long userId = customUserDetails.getUserId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();

		/*String token = jwtUtil.createJwt(username, userId, role, 60*60*24*1000L);
		System.out.println(token);


		response.addCookie(createCookie("Authorization", token));


		response.sendRedirect("http://localhost:3000/");*/

		// Generate JWT token
		String token = jwtUtil.createJwt(username, userId, role, 60 * 60 * 24 * 1000L);
		System.out.println(token);

		// Determine environment
		boolean isProductionEnvironment = request.isSecure(); // This checks if the request is HTTPS

		// Create Set-Cookie header
		String cookieHeader = String.format(
			"Authorization=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=%s; %s",
			token,
			60 * 60 * 24, // 1 day in seconds
			isProductionEnvironment ? "None" : "None",
			isProductionEnvironment ? "Secure" : ""
		);

		// Add the Set-Cookie header to the response
		response.setHeader("Set-Cookie", cookieHeader);

		// Redirect to the frontend
		response.sendRedirect("http://localhost:3000/");

	}

	private Cookie createCookie(String key, String value) {

		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60*60*24);
		//cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}
}
