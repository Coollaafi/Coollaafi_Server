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
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "존재하지 않는 유저입니다."),
    SENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER002", "Sender가 존재하지 않습니다"),
    RECEIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER003", "Receiver가 존재하지 않습니다."),

    //POST
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST001", "존재하지 않는 게시글입니다."),

    //POST-PREFER
    POST_PREFER_NOT_FOUND(HttpStatus.NOT_FOUND, "POSTPREFER001", "존재하지 않는 좋아요입니다."),

    //FRIENDS
    FRIEND_REQUEST_ALREADY_EXIST(HttpStatus.NOT_FOUND, "FRIEND001", "이미 존재하는 친구 요청입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND002", "존재하지 않는 친구 요청입니다."),
    FRIEND_REQUEST_ALREADY_PROCESSED(HttpStatus.NOT_FOUND, "FRIEND002", "이미 완료된 친구 요청입니다.")


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
