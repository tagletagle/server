package com.example.tagletagle.oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDTO {

	Long userId;
	String username;
	String name;
	String role;
	Boolean isNewUser;

}
