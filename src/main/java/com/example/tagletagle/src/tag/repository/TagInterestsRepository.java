package com.example.tagletagle.src.tag.repository;

import com.example.tagletagle.src.tag.entity.UserTagInterests;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagInterestsRepository extends JpaRepository<UserTagInterests, Long> {
    @Query("SELECT u FROM UserTagInterests u WHERE u.user.id = :userId")
    List<UserTagInterests> findUserTagInterestsByUser(@Param("userId") Long userId);
}
