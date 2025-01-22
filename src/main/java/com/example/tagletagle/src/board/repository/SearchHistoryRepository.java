package com.example.tagletagle.src.board.repository;

import com.example.tagletagle.src.board.entity.SearchHistoryEntity;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {
    @Query("SELECT s FROM SearchHistoryEntity s WHERE s.user.id = :userId")
    List<SearchHistoryEntity> findSearchHistoryEntitiesByUser_Id(@Param("userId") Long userId);

}
