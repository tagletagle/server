package com.example.tagletagle.src.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findUserEntityByUsernameAndStatus(String username, Status status);

	Optional<UserEntity> findUserEntityByIdAndStatus(Long userId, Status status);

	boolean existsUserEntityByNickname(String nickname);

}
