package com.example.tagletagle.src.user.service;

import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	//유효성 검사
	private final UserRepository userRepository;


	public void saveOrUpdateUserBasicInfo(Long userId, UserBasicInfoDTO userBasicInfoDTO) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));


		// 로직 -> user 찾은 다음, user의 닉네임/한줄소개/생년월일/성별 바꾸기
		user.setNickname(userBasicInfoDTO.getNickname());
		user.setDescription(userBasicInfoDTO.getDescription());
		user.setBirthDate(userBasicInfoDTO.getBirthDate());
		user.setGender(userBasicInfoDTO.getGender());

		userRepository.save(user);

	}
}
