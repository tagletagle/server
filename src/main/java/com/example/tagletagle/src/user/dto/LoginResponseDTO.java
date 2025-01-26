package com.example.tagletagle.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 후 응답을 담은 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

	private Boolean isNewUser;
	private String accessToken;
	private String refreshToken;

}
