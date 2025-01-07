package com.example.tagletagle.oauth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.tagletagle.config.Status;
import com.example.tagletagle.oauth.dto.CustomOAuth2User;
import com.example.tagletagle.oauth.dto.GoogleResponse;
import com.example.tagletagle.oauth.dto.NaverResponse;
import com.example.tagletagle.oauth.dto.OAuth2Response;
import com.example.tagletagle.oauth.dto.OAuth2UserDTO;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		//로그인한 사용자 정보 가져오기
		OAuth2User oAuth2User = super.loadUser(userRequest);

		System.out.println(oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("naver")) {

			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		}
		else if (registrationId.equals("google")) {

			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		}
		else {

			return null;
		}

		String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

		UserEntity user = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE)
			.orElse(null);

		OAuth2UserDTO oauth2UserDTO;
		if(user == null){
			user = new UserEntity(username, "ROLE_USER");
			userRepository.save(user);

			oauth2UserDTO = new OAuth2UserDTO();
			oauth2UserDTO.setUserId(user.getId());
			oauth2UserDTO.setUsername(username);
			//oauth2UserDTO.setName(oauth2UserDTO.getName());
			oauth2UserDTO.setName("user");
			oauth2UserDTO.setRole("ROLE_USER");
			oauth2UserDTO.setIsNewUser(Boolean.TRUE);
		}

		else{
			oauth2UserDTO = new OAuth2UserDTO();
			oauth2UserDTO.setUserId(user.getId());
			oauth2UserDTO.setUsername(username);
			//oauth2UserDTO.setName(oauth2UserDTO.getName());
			oauth2UserDTO.setName("user");
			oauth2UserDTO.setRole("ROLE_USER");
			oauth2UserDTO.setIsNewUser(Boolean.FALSE);
		}

		return new CustomOAuth2User(oauth2UserDTO);
	}


}
