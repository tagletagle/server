package com.example.tagletagle.src.board.repository;

import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchResultRepository {

    String getType();
    String getNickNameAndTagName();
}
