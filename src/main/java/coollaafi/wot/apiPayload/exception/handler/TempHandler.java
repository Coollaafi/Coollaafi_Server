package coollaafi.wot.apiPayload.exception.handler;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
