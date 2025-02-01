package Spring.MindStone.repository.memberRepository;

import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.member.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {
    Optional<MemberInterest> findByMemberInfo(MemberInfo memberInfo);

    MemberInterest getMemberInterestById(Long memberId);
}
