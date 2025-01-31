package Spring.MindStone.repository.memberRepository;

import Spring.MindStone.domain.member.MemberInterest;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {
    Optional<MemberInterest> findByMemberInfo(MemberInfo memberInfo);
}
