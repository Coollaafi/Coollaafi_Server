package coollaafi.wot.web.ootdImage;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class OotdImageHandler extends GeneralException {
    public OotdImageHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
