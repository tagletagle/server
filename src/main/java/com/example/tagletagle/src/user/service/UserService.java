package com.example.tagletagle.src.user.service;

import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.user.dto.FollowsDTO;
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
	private final UserRepository userRepository;
	private final FollowsRepository followsRepository;


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

		UserEntity followingUser = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		Boolean isFollow = followsRepository.existsByFollowerAndFollowing(user, followingUser);

		String comment = null;

		if (isFollow == Boolean.FALSE) {
			FollowsEntity followsEntity = new FollowsEntity(user, followingUser);
			followsRepository.save(followsEntity);

			user.increaseFollowingCount();
			followingUser.increaseFollowerCount();

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

	public List<FollowsDTO> getFollowingUsers(Long follower) {
		return followsRepository.findFollowingByFollower(follower).stream()
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
