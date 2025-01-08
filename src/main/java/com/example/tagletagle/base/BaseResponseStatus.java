package com.example.tagletagle.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

	/**
	 * 200 : 요청 성공
	 */
	Success(true, 200, "ok"),

	/**
	 * 2000 : 임준현 에러
	 */

	/**
	 * 3000 : 조윤재 에러
	 */

	/**
	 * 4000 : 박윤아 에러
	 */

	/**
	 * 5000 : global
	 */
	DATA_CONFLICT(false, 5000, "데이터 충돌로 업데이트 실패"),
	NO_EXIST_TOKEN(false,5100, "토큰이 존재하지 않습니다"),
	EXPIRED_TOKEN(false, 5110, "토큰 시간이 만료되었습니다"),
	INVALID_TOKEN(false,5120, "토큰이 올바르지 않습니다");

	private final boolean isSuccess;
	private final int code;
	private final String message;

}
