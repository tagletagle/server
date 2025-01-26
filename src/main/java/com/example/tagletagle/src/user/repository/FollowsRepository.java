package com.example.tagletagle.src.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

import javax.sql.DataSource;
import java.util.List;

public interface FollowsRepository extends JpaRepository<FollowsEntity, Long> {

	Boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);

	@Modifying
	@Transactional
	@Query("DELETE FROM FollowsEntity f WHERE f.follower = :follower AND f.following =:following")
	int deleteFollowsByUserAndUser(@Param("follower") UserEntity follower, @Param("following") UserEntity following);

	@Query("SELECT f FROM FollowsEntity f WHERE f.follower.id = :follower")
	List<FollowsEntity> findFollowingByFollower(@Param("follower") Long follower);






}