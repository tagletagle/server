package com.example.tagletagle.oauth.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private OAuth2UserDTO oauth2UserDTO;

	public CustomOAuth2User(OAuth2UserDTO oauth2UserDTO){
		this.oauth2UserDTO = oauth2UserDTO;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {

				return oauth2UserDTO.getRole();
			}
		});

		return collection;
	}

	@Override
	public String getName() {

		return oauth2UserDTO.getName();
	}

	public String getUsername() {

		return oauth2UserDTO.getUsername();
	}

	public Long getUserId(){
		return oauth2UserDTO.getUserId();
	}
}
