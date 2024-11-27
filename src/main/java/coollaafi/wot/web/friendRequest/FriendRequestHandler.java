package coollaafi.wot.web.friendRequest;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class FriendRequestHandler extends GeneralException {
    public FriendRequestHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
