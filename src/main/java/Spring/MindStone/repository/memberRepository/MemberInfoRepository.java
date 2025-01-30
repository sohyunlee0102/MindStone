package Spring.MindStone.repository.memberRepository;

import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    boolean existsByEmail(String email);
    Optional<MemberInfo> findByEmail(String email);     // 이메일로 회원 정보 조회
}
