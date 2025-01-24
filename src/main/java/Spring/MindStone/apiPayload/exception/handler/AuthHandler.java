package Spring.MindStone.apiPayload.exception.handler;

import Spring.MindStone.apiPayload.code.BaseErrorCode;
import Spring.MindStone.apiPayload.exception.GeneralException;

public class AuthHandler extends GeneralException {

    public AuthHandler(BaseErrorCode errorCode) { super(errorCode); }
}
