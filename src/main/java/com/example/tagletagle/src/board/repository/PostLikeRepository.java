package com.example.tagletagle.src.board.repository;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.board.entity.PostLikeEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

	Boolean existsByPostAndUser(PostEntity post, UserEntity user);

	@Modifying
	@Transactional
	@Query("DELETE FROM PostLikeEntity pl WHERE pl.user = :user AND pl.post =:post")
	int deleteUserAllergy(@Param("user") UserEntity user, @Param("post") PostEntity post);


}
