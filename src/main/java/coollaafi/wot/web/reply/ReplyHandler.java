package coollaafi.wot.web.reply;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class ReplyHandler extends GeneralException {
    public ReplyHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
