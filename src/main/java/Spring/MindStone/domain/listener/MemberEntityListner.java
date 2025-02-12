package Spring.MindStone.domain.listener;

import Spring.MindStone.apiPayload.code.status.ErrorStatus;
import Spring.MindStone.apiPayload.exception.handler.MemberInfoHandler;
import Spring.MindStone.domain.enums.Status;
import Spring.MindStone.domain.member.MemberInfo;
import jakarta.persistence.PostLoad;

public class MemberEntityListner {

    @PostLoad
    public void filterInactiveMembers(MemberInfo member) {
        if (member.getStatus() == Status.INACTIVE) {
            System.out.println("Inactive Member");
            throw new MemberInfoHandler(ErrorStatus.INACTIVE_MEMBER);
        }
    }
}
