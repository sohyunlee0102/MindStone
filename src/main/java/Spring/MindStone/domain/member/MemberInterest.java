package Spring.MindStone.domain.member;

import Spring.MindStone.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo memberInfo; // 회원 ID (FK, Member와 관계 설정)

    @Column(name = "hobby_actions")
    private String hobbyActions;   // 취미 행동 (운동, 독서 등)

    @Column(name = "hobby_action_count")
    private Integer hobbyActionCount;   // 취미 선택 횟수

    @Column(name = "special_skill_actions")
    private String specialSkillActions; // 특기 행동 (글쓰기, 요리 등)

    @Column(name = "special_skill_action_count")
    private Integer specialSkillActionCount;    // 특기 선택 횟수

    @Column(name = "stress_actions")
    private String stressActions;       // 스트레스 풀이 방법(행동)

    @Column(name = "stress_actions_count")
    private Integer stressActionsCount; // 스트레스 풀이 선택 횟수
}
