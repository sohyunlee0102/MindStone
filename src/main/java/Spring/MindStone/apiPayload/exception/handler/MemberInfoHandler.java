package Spring.MindStone.apiPayload.exception.handler;

import Spring.MindStone.apiPayload.code.BaseErrorCode;
import Spring.MindStone.apiPayload.exception.GeneralException;

public class MemberInfoHandler extends GeneralException {

    public MemberInfoHandler(BaseErrorCode errorCode) { super(errorCode); }

}
