package Spring.MindStone.repository;

import Spring.MindStone.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    boolean existsByEmail(String email);
    Optional<MemberInfo> findByEmail(String email);
}
