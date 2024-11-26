package coollaafi.wot.apiPayload.code.status;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "존재하지 않는 유저입니다."),
    FOLLOWER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER002", "팔로워 회원이 존재하지 않습니다"),
    FOLLOWEE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER003", "팔로우할 회원이 존재하지 않습니다."),

    //FOLLOW
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW001", "팔로우 기록이 존재하지 않습니다."),
    FOLLOWER_EQUAL_FOLLOWEE(HttpStatus.NOT_FOUND, "FOLLOW002", "자기 자신을 팔로우 할 수 없습니다."),
    LIMIT_FOLLOW(HttpStatus.NOT_FOUND, "FOLLOW003", "팔로우는 최대 20명까지만 가능합니다."),
    ALREADY_FOLLOWING(HttpStatus.NOT_FOUND, "FOLLOW004", "이미 팔로우 중입니다."),

    //POST
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST001", "존재하지 않는 게시글입니다."),

    //POST-PREFER
    POST_PREFER_NOT_FOUND(HttpStatus.NOT_FOUND, "POSTPREFER001", "존재하지 않는 좋아요입니다."),

    //COMMENT
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT001", "존재하지 않는 댓글입니다."),

    //OOTD
    OOTD_NOT_FOUND(HttpStatus.NOT_FOUND, "OOTD001", "존재하지 OOTD 이미지입니다."),

    //추가
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}
