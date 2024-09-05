package coollaafi.wot.web.postPrefer;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class PostPreferHandler extends GeneralException {
    public PostPreferHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
