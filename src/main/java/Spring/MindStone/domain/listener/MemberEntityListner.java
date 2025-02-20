package Spring.MindStone.domain.listener;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.enums.Status;
import Spring.MindStone.domain.member.MemberInfo;
import jakarta.persistence.PostLoad;

import java.time.LocalDate;

public class MemberEntityListner {

    @PostLoad
    public void filterInactiveMembers(MemberInfo member) {
        if (!ListenerUtil.isListenerEnabled()) {
            return;
        }

        if (member.getStatus() == Status.INACTIVE) {
            throw new MemberInfoHandler(ErrorStatus.INACTIVE_MEMBER);
        }
    }

}

