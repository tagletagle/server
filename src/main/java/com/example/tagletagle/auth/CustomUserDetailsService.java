package com.example.tagletagle.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//DB에서 조회
		UserEntity userEntity = userRepository.findUserEntityByUsernameAndStatus(username, Status.ACTIVE).
			orElse(null);

		Boolean isNewUser;

		//신규 유저
		if(userEntity == null){
			userEntity = new UserEntity(username);
			userEntity.setPassword(bCryptPasswordEncoder.encode("tagle1234"));


			userRepository.save(userEntity);

			isNewUser = Boolean.TRUE;
			System.out.println("555555");

		}else{
			isNewUser = Boolean.FALSE;
		}

		return new CustomUserDetails(userEntity, isNewUser);
	}
}