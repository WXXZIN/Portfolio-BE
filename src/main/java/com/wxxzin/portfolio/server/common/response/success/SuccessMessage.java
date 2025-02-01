package com.wxxzin.portfolio.server.common.response.success;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    /* AUTH - USER */
    AUTH_LOGIN_SUCCESS("로그인 성공"), 
    AUTH_LOGOUT_SUCCESS("로그아웃 성공"),
    AUTH_USERNAME_FIND_SUCCESS("아이디 찾기 성공"),
    AUTH_PASSWORD_CHANGE_SUCCESS("비밀번호 변경 성공"),
    AUTH_TEMP_PASSWORD_CHANGE_SUCCESS("임시 비밀번호 변경 성공"),
    AUTH_TOKEN_REISSUE_SUCCESS("토큰 갱신 성공"),
    
    /* AUTH - EMAIL */
    AUTH_VERIFICATION_EMAIL_SEND_SUCCESS("인증 메일 전송 성공"),
    AUTH_TEMP_PASSWORD_EMAIL_SEND_SUCCESS("임시 비밀번호 전송 성공"),
    AUTH_EMAIL_VERIFICATION_SUCCESS("이메일 인증 성공"),

    /* AUTH - DEVICE */
    AUTH_DEVICE_REGISTER_SUCCESS("기기 등록 성공"),
    AUTH_DEVICE_LIST_RETRIEVE_SUCCESS("기기 목록 조회 성공"),
    AUTH_DEVICE_INFO_RETRIEVE_SUCCESS("기기 정보 조회 성공"),
    AUTH_DEVICE_DELETE_SUCCESS("기기 삭제 성공"),

    /* USER */
    USER_REGISTER_SUCCESS("회원가입 성공"),
    USER_USERNAME_CHECK_SUCCESS("아이디 중복 확인 성공"),
    USER_NICKNAME_CHECK_SUCCESS("닉네임 중복 확인 성공"),
    USER_INFO_RETRIEVE_SUCCESS("회원 정보 조회 성공"),
    USER_NICKNAME_CHANGE_SUCCESS("닉네임 변경 성공"),
    USER_DELETE_SUCCESS("회원 탈퇴 성공"),


    /* TEAM */
    TEAM_CREATE_SUCCESS("팀 생성 성공"),
    TEAM_LIST_RETRIEVE_SUCCESS("팀 목록 조회 성공"),
    TEAM_INFO_RETRIEVE_SUCCESS("팀 정보 조회 성공"),
    TEAM_LEADER_CHANGE_SUCCESS("팀장 변경 성공"),
    TEAM_LEAVE_SUCCESS("팀 나가기 성공"),

    /* TEAM - APPLICATION */
    TEAM_APPLICATION_APPLY_SUCCESS("팀 신청 성공"),
    TEAM_APPLICATION_LIST_RETRIEVE_SUCCESS("팀 신청 목록 조회 성공"),
    TEAM_APPLICATION_APPROVE_SUCCESS("팀 신청 수락 성공"),
    TEAM_APPLICATION_REJECT_SUCCESS("팀 신청 거절 성공"),
    TEAM_APPLICATION_DELETE_SUCCESS("팀 신청 삭제 성공"),

    /* TEAM - TASK */
    TEAM_TASK_ADD_SUCCESS("팀 업무 추가 성공"),
    TEAM_TASK_LIST_RETRIEVAL_SUCCESS("팀 업무 목록 조회 성공"),
    TEAM_TASK_INFO_RETRIEVAL_SUCCESS("팀 업무 정보 조회 성공"),
    TEAM_TASK_EDIT_SUCCESS("팀 업무 수정 성공"),
    TEAM_TASK_DELETE_SUCCESS("팀 업무 삭제 성공"),

    /* PROJECT */
    PROJECT_WRITE_SUCCESS("프로젝트 작성 성공"),
    PROJECT_LIST_RETRIEVE_SUCCESS("프로젝트 목록 조회 성공"),
    PROJECT_INFO_RETRIEVE_SUCCESS("프로젝트 정보 조회 성공"),
    PROJECT_EDIT_SUCCESS("프로젝트 수정 성공"),
    PROJECT_DELETE_SUCCESS("프로젝트 삭제 성공"),

    /* PROJECT - HEART */
    PROJECT_HEART_SUCCESS("프로젝트 좋아요 성공"),
    PROJECT_HEART_CANCEL_SUCCESS("프로젝트 좋아요 취소 성공"),

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
