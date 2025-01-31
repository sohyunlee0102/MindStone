package Spring.MindStone.service.emotionWayService;

import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.member.MemberInterest;
import Spring.MindStone.repository.memberRepository.MemberInfoRepository;
import Spring.MindStone.repository.memberRepository.MemberInterestRepository;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayRequestDto;
import Spring.MindStone.web.dto.emotionWayDto.EmotionWayResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmotionWayService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberInterestRepository memberInterestRepository;

    /**
     * 감정 관리 방법 조회
     */
    public EmotionWayResponseDto getEmotionWay(Long memberId) {
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        MemberInterest memberInterest = memberInterestRepository.findByMemberInfo(member)
                .orElseThrow(() -> new EntityNotFoundException("회원의 감정 관리 정보를 찾을 수 없습니다."));

        return EmotionWayResponseDto.builder()
                .mbti(member.getMbti())
                .job(member.getJob())
                .hobby(memberInterest.getHobbyActions())
                .strengths(memberInterest.getSpecialSkillActions())
                .stressManagement(memberInterest.getStressActions())
                .build();
    }

    /**
     * 감정 관리 방법 수정
     */
    @Transactional
    public void updateEmotionWay(Long memberId, EmotionWayRequestDto dto) {
        MemberInfo member = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        MemberInterest memberInterest = memberInterestRepository.findByMemberInfo(member)
                .orElseThrow(() -> new EntityNotFoundException("회원의 감정 관리 정보를 찾을 수 없습니다."));

        // 정보 업데이트
        member.setMbti(dto.getMbti());
        member.setJob(dto.getJob());
        memberInterest.setHobbyActions(dto.getHobby());
        memberInterest.setSpecialSkillActions(dto.getStrengths());
        memberInterest.setStressActions(dto.getStressManagement());

        memberInfoRepository.save(member);
        memberInterestRepository.save(memberInterest);
    }
}
