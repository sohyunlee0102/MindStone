package Spring.MindStone.apiPayload.exception.handler;

import Spring.MindStone.apiPayload.code.BaseErrorCode;
import Spring.MindStone.apiPayload.exception.GeneralException;

public class HabitHandler extends GeneralException {

    public HabitHandler(BaseErrorCode errorCode) { super(errorCode); }

}
