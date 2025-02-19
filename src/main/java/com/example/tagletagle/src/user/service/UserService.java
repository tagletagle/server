package com.example.tagletagle.src.user.service;


import com.example.tagletagle.config.NotificationType;
import com.example.tagletagle.src.notification.entity.NotificationEntity;
import com.example.tagletagle.src.notification.repository.NotificationRepository;
import com.example.tagletagle.src.user.dto.UserProfileResponseDTO;

import com.example.tagletagle.src.user.dto.FollowsDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.FollowsRepository;
import com.example.tagletagle.src.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	//유효성 검사
	@Autowired
	private final UserRepository userRepository;
	private final FollowsRepository followsRepository;
	private final NotificationRepository notificationRepository;


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

	@Transactional
	public String followUser(Long userId, Long followingUserId) {
		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		UserEntity followingUser = userRepository.findUserEntityByIdAndStatus(followingUserId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		Boolean isFollow = followsRepository.existsByFollowerAndFollowing(user, followingUser);

		String comment = null;

		if (isFollow == Boolean.FALSE) {
			FollowsEntity followsEntity = new FollowsEntity(user, followingUser);
			followsRepository.save(followsEntity);

			user.increaseFollowingCount();
			followingUser.increaseFollowerCount();

			NotificationEntity notification = new NotificationEntity(NotificationType.FOLLOW, followingUser ,user);
			notificationRepository.save(notification);

			comment = "팔로잉 되었습니다.";
		}
		else if(isFollow == Boolean.TRUE){
			followsRepository.deleteFollowsByUserAndUser(user, followingUser);

			comment = "팔로잉이 해제되었습니다";

		}

		return comment;


	}


	public boolean nicknameDupCheck(String nickname){
		return userRepository.existsUserEntityByNickname(nickname);

	}


	public UserProfileResponseDTO getUserProfile(Long userId) {
		return userRepository.findById(userId)
				.map(user -> new UserProfileResponseDTO(

						//로직 -> user id로 찾은 다음, user의 닉네임/한줄소개/팔로우/팔로워/태그/프로필 url 가져오기
						user.getId(),
						user.getUsername(),
						user.getNickname(),
						user.getDescription(),
						user.getFollowerCount(),
						user.getFollowingCount(),
						user.getProfileImgUrl()

				))
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
	}

	public List<FollowsDTO> getFollowingUsers(Long user_id) {
		return followsRepository.findFollowingByFollower(user_id).stream()
				.map(this::convertToFollowsDTO)
				.collect(Collectors.toList());
	}

	public List<FollowsDTO> getFollowerUsers(Long following_user_id) {
		return followsRepository.findFollowerByFollowing(following_user_id).stream()
				.map(this::convertToFollowsDTO)
				.collect(Collectors.toList());
	}

	private FollowsDTO convertToFollowsDTO(FollowsEntity followsEntity) {
		FollowsDTO followsDTO = new FollowsDTO();

		followsDTO.setId((followsEntity.getId()));
		followsDTO.setFollowerId(followsEntity.getFollower().getId());
		followsDTO.setFollowingId(followsEntity.getFollowing().getId());
		followsDTO.setFollowingNickname(followsEntity.getFollowing().getNickname());
		followsDTO.setFollowingProfile(followsEntity.getFollowing().getProfileImgUrl());

		return followsDTO;
	}

}
