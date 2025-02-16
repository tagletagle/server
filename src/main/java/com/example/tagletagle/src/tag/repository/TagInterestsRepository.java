package com.example.tagletagle.src.tag.repository;

import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.entity.UserTagInterests;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagInterestsRepository extends JpaRepository<UserTagInterests, Long> {
    @Query("SELECT u FROM UserTagInterests u WHERE u.user.id = :userId")
    List<UserTagInterests> findUserTagInterestsByUser(@Param("userId") Long userId);

    @Query("SELECT ut FROM UserTagInterests ut WHERE ut.id IN :interestTagId")
    List<UserTagInterests> findAllByIds(List<Long> interestTagId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_tag_interests (user_id, tag_id) VALUES (:userId, :tagId)", nativeQuery = true)
    void insertUserInterestTag(Long userId, Long tagId);

    Boolean existsByUserAndTag(UserEntity user, TagEntity tag);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserTagInterests ut WHERE ut.user = :user AND ut.tag =:tag")
    int deleteUserTagInterestsByUserAndTag(@Param("user") UserEntity user, @Param("tag") TagEntity tag);
}
