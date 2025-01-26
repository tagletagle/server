package com.example.tagletagle.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "access token, refresh token 응답 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

	private String accessToken;
	private String refreshToken;

}
