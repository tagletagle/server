package com.example.tagletagle.src.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class FollowsDTO {
    private Long id;
    private Long followerId;
    private Long followingId;
    private String followingNickname;
    private String followingProfile;

}
