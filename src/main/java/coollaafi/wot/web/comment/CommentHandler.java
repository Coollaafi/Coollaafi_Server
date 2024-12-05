package coollaafi.wot.web.comment;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class CommentHandler extends GeneralException {
  public CommentHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
