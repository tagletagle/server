package com.example.tagletagle.src.user.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor

public class UserProfileResponseDTO {

    private Long id;
    private String nickname;
    private String username;
    private String description;
    private Long follower_count;
    private Long following_count;
    private String profileImgUrl;

}
