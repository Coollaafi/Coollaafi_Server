package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
