package Spring.MindStone.repository.emotionNoteRepository;

import Spring.MindStone.domain.emotion.EmotionNote;
import Spring.MindStone.domain.emotion.StressEmotionNote;
import Spring.MindStone.domain.member.MemberInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StressEmotionNoteRepository extends JpaRepository<StressEmotionNote, Long> {
    @Query("SELECT e FROM StressEmotionNote e WHERE e.memberInfo = :memberInfo AND e.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<StressEmotionNote> findByIdAndCreatedAtBetween
            (@Param("memberInfo") MemberInfo memberInfo,
             @Param("startOfDay") LocalDateTime startOfDay,
             @Param("endOfDay") LocalDateTime endOfDay,
             Sort sort);

    @Query("SELECT COUNT(hh) FROM StressEmotionNote hh " +
            "WHERE hh.memberInfo.id = :memberId " +
            "AND YEAR(hh.createdAt) = :year " +
            "AND MONTH(hh.createdAt) = :month")
    int countStressEmotionNoteByMonth(@Param("memberId") Long memberId,
                                    @Param("year") int year,
                                    @Param("month") int month);
}
