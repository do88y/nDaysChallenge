package challenge.nDaysChallenge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomError implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 멤버가 존재하지 않습니다."),
    DAJIM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 다짐이 존재하지 않습니다."),
    SPECIFIC_DAJIM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 멤버와 룸에 등록된 다짐이 존재하지 않습니다."),
    ROOM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 룸이 존재하지 않습니다."),
    EMOTION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 이모션이 존재하지 않습니다."),

    EXISTING_NICKNAME(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 닉네임입니다."),
    EXISTING_ID(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 아이디입니다."),
    EXISTING_EMOTION(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 이모션입니다."),
    EXISTING_DAJIM(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 다짐입니다."),

    WRONG_REGEX(HttpStatus.INTERNAL_SERVER_ERROR, "정해진 비밀번호 형식에 맞게 입력해주세요."),
    WRONG_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 비밀번호입니다."),
    NULL_INPUT(HttpStatus.INTERNAL_SERVER_ERROR, "값을 입력해주세요."),

    USER_LOGOUT(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃된 사용자입니다."),

    TOKEN_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰을 찾을 수 없습니다."),
    EXPIRED_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰이 유효하지 않습니다."),
    WRONG_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰의 사용자 정보가 일치하지 않습니다."),
    NO_DATA_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "권한 정보가 없는 토큰입니니다."),

    AUTHENTICATION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication 객체를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
