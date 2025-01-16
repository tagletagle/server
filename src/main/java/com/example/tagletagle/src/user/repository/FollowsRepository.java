package com.example.tagletagle.src.user.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

public interface FollowsRepository extends JpaRepository<FollowsEntity, Long> {

	Boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);

	@Modifying
	@Transactional
	@Query("DELETE FROM FollowsEntity f WHERE f.follower = :follower AND f.following =:following")
	int deleteFollowsByUserAndUser(@Param("follower") UserEntity follower, @Param("following") UserEntity following);
}
