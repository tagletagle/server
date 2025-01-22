package com.example.tagletagle.src.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class FollowsDTO {
    Long id;
    Long followerId;
    Long followingId;

}
