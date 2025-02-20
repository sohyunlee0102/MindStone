package Spring.MindStone.repository.emotionNoteRepository;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmotionNoteRepository extends JpaRepository<EmotionNote, Long> {
    @Query("SELECT e FROM EmotionNote e WHERE e.memberInfo = :memberInfo AND e.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<EmotionNote> findByMemberInfoAndCreatedAtBetween(
            @Param("memberInfo") MemberInfo memberInfo,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Sort sort
    );
}
