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
	USER_NO_EXIST(false, 2000, "유저가 존재하지 않습니다"),
	AUTHOR_NO_EXIST(false, 2001, "저자가 존재하지 않습니다"),

	POST_NO_EXIST(false, 2100, "게시글이 존재하지 않습니다"),
	MUST_WRITE_URL(false, 2105, "게시글 작성 시 url 작성은 필수입니다"),
	MUST_SELECT_TAG(false, 2110, "게시글 작성 시 태그 선택은 필수입니다"),
	NO_VALID_LOGINDTO(false, 2200, "로그인 파라미터를 다시 확인해주세요"),
	FAILED_LOGIN(false, 2205, "로그인 파라미터 값이 올바르지 않습니다"),
	INVALID_ACCESS_TOKEN(false,2300, "access 토큰이 올바르지 않습니다"),
	INVALID_REFRESH_TOKEN(false,2301, "refresh 토큰이 올바르지 않습니다"),
	EXPIRED_ACCESS_TOKEN(false, 2310, "access 토큰 시간이 만료되었습니다"),
	EXPIRED_REFRESH_TOKEN(false, 2311, "access 토큰 시간이 만료되었습니다"),
	NO_BEARER_FORM(false, 2320, "Beraer 형식이 아닙니다"),




	/**
	 * 3000 : 조윤재 에러
	 */
	TAG_NO_EXIST(false, 3000, "태그가 존재하지 않습니다."),

	/**
	 * 4000 : 박윤아 에러
	 */

	/**
	 * 5000 : global
	 */
	DATA_CONFLICT(false, 5000, "데이터 충돌로 업데이트 실패"),
	HEADER_ERROR(false,5100, "헤더 값을 확인해주세요"),
	PARAMETER_ERROR(false, 5110, "파라미터 값을 다시 확인해주세요"),

	REQUIRED_LOGIN(false, 5200, "로그인이 필요합니다"),


	UNEXPECTED_ERROR(false, 6000, "예상치 못한 오류가 발생했습니다");

	private final boolean isSuccess;
	private final int code;
	private final String message;

}
