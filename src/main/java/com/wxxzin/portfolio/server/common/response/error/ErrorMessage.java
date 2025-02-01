package com.wxxzin.portfolio.server.common.response.error;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    /* COMMON */
    UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다."),
    CREATION_FAILED("생성에 실패했습니다."),
    RETRIEVAL_FAILED("조회에 실패했습니다."),
    MODIFICATION_FAILED("수정에 실패했습니다."),
    REMOVAL_FAILED("삭제에 실패했습니다."),
    DATA_NOT_FOUND("데이터를 찾을 수 없습니다."),

    /* AUTH - USER */
    USER_AUTH_FAILED("사용자 인증에 실패했습니다."),
    USER_LOGIN_FAILED("로그인에 실패했습니다."),

    /* AUTH - DETAILS(USER) */
    NOT_SUPPORTED_PROVIDER("지원하지 않는 인증 제공자입니다."),
    INVALID_CREDENTIALS("잘못된 계정 정보입니다."),
    INVALID_REQUEST_BODY("요청 본문이 잘못되었습니다."),
    USERNAME_NOT_MATCH("아이디가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_BEFORE("새로운 비밀번호가 이전 비밀번호와 동일합니다."),
    
    /* AUTH - DETAILS(TOKEN) */
    MALFORMED_TOKEN("잘못된 토큰 형식입니다."),
    INVALID_SIGNATURE("잘못된 서명입니다."),
    INVALID_CLAIMS("잘못된 클레임입니다."),
    ACCESS_TOKEN_NOT_FOUND("Access Token이 존재하지 않습니다."),
    INVALID_ACCESS_TOKEN("Access Token이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN("Access Token이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND("Refresh Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN("Refresh Token이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN("Refresh Token이 만료되었습니다."),

    /* AUTH - DETAILS(DEVICE) */
    DEVICE_NOT_FOUND("기기가 존재하지 않습니다."),
    
    /* EMAIL */
    EMAIL_SEND_FAILED("이메일 전송에 실패했습니다."),
    EMAIL_CERTIFICATION_FAILED("이메일 인증에 실패했습니다."),

    /* EMAIL - DETAILS */
    INVALID_EMAIL("이메일이 유효하지 않습니다."),
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
    EMAIL_BLOCKED("이메일 요청이 차단되었습니다."),
    EMAIL_REQUEST_LIMIT("이메일 요청 제한에 도달했습니다."),
    EMAIL_REQUEST_INTERVAL("이메일 요청 간격이 너무 짧습니다."),
    EMAIL_REQUEST_EXCEEDED("이메일 요청 횟수를 초과했습니다."),
    CERTIFICATION_NUMBER_IS_EMPTY("인증 번호가 비어 있습니다."),
    INVALID_CERTIFICATION_NUMBER("인증 번호가 유효하지 않습니다."),

    /* USER */
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    USER_IS_TEAM_LEADER_OR_MEMBER("현재 팀에 속한 사용자입니다."),
    USER_DELETE_FAILED("사용자 삭제에 실패했습니다."),
    USERNAME_ALREADY_EXISTS("이미 존재하는 사용자 이름입니다."),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임입니다."),
    
    /* TEAM */
    TEAM_NOT_FOUND("존재하지 않는 팀입니다."),
    NOT_TEAM_LEADER("팀장이 아닙니다."),
    NOT_TEAM_MEMBER("팀원이 아닙니다."),
    LEADER_CANNOT_LEAVE_TEAM("팀장은 팀을 나갈 수 없습니다."),

    /* TEAM - APPLICATION */
    TEAM_APPLICATION_NOT_FOUND("존재하지 않는 팀 신청입니다."),
    TEAM_ALREADY_APPLIED("이미 신청한 팀입니다."),
    TEAM_NOT_RECRUITING("팀이 모집 중이 아닙니다."),
    TEAM_LEADER_CANNOT_APPLY("팀장은 팀에 신청할 수 없습니다."),
    TEAM_APPLICATION_ALREADY_EXISTS("이미 신청한 팀입니다."),
    NOT_TEAM_APPLICATION_OWNER("팀 신청자가 아닙니다."),

    /* TEAM - TASK */
    NOT_FOUND_TASK("존재하지 않는 업무입니다."),

    /* PROJECT */
    PROJECT_ALREADY_RECRUITING("프로젝트가 이미 모집 중입니다."),
    PROJECT_NOT_FOUND("존재하지 않는 프로젝트입니다."),
    NOT_PROJECT_OWNER("프로젝트 소유자가 아닙니다."),

    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
