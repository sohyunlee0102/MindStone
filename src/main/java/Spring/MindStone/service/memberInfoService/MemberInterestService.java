package Spring.MindStone.service.memberInfoService;

import Spring.MindStone.domain.member.MemberInterest;
import Spring.MindStone.repository.memberRepository.MemberInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInterestService {
    private final MemberInterestRepository memberInterestRepository;

    public String getStressActionMine(Long memberId){
        MemberInterest memberInterest = memberInterestRepository.getMemberInterestById(memberId);
        List<String> actionList = Arrays.asList(memberInterest.getStressActions().split(","));
        return null;
    }
}
