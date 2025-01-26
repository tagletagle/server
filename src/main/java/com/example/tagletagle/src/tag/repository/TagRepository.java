package com.example.tagletagle.src.tag.repository;

import java.util.List;
import java.util.Optional;

import com.example.tagletagle.src.user.entity.FollowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tagletagle.src.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("SELECT t FROM TagEntity t WHERE t.name LIKE %:word%")
    List<TagEntity> findTagsByName(@Param("word")String word);


    List<TagEntity> findTop10ByOrderByUpdateAtDesc();
}

