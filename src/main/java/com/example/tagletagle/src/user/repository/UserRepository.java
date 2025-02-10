package com.example.tagletagle.src.user.repository;

import java.util.List;
import java.util.Optional;

import com.example.tagletagle.src.tag.entity.UserTagInterests;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findUserEntityByUsernameAndStatus(String username, Status status);

	Optional<UserEntity> findUserEntityByIdAndStatus(Long userId, Status status);

	boolean existsUserEntityByNickname(String nickname);

	List<UserEntity> findByNicknameContainingOrUsernameContainingOrDescriptionContaining(String nickname, String username, String description);

	@Query("SELECT ut FROM UserTagInterests ut JOIN ut.user u WHERE u.id = :userId")
	List<UserTagInterests> findUserTagInterestsById(@Param("userId") Long userId);
}
