package Spring.MindStone.domain.member;

import Spring.MindStone.domain.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "회원 취미, 특기, 스트레스 해소 행동 방식 엔티티")
public class MemberInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo member; // 회원 ID (FK)

    @ElementCollection
    @CollectionTable(name = "hobby_actions", joinColumns = @JoinColumn(name = "member_interest_id"))
    @Column(name = "action")
    private List<String> hobbyActions;  // 취미 행동 (운동, 독서 등)

    @Transient  // 엔티티에서는 계산된 필드로 저장되지 않도록 설정
    private Integer hobbyActionCount;   // 취미 선택 횟수

    @ElementCollection
    @CollectionTable(name = "special_skill_actions", joinColumns = @JoinColumn(name = "member_interest_id"))
    @Column(name = "action")
    private List<String> specialSkillActions; // 특기 행동 (글쓰기, 요리 등)

    @Transient
    private Integer specialSkillActionCount;    // 특기 선택 횟수

    @ElementCollection
    @CollectionTable(name = "stress_actions", joinColumns = @JoinColumn(name = "member_interest_id"))
    @Column(name = "action")
    private List<String> stressActions;       // 스트레스 풀이 방법(행동)

    @Transient
    private Integer stressActionsCount; // 스트레스 풀이 선택 횟수

    // 저장 직전이나 수정 직전에 횟수를 자동으로 계산
    @PrePersist
    @PreUpdate
    public void calculateCounts() {
        this.hobbyActionCount = (this.hobbyActions != null) ? this.hobbyActions.size() : 0;
        this.specialSkillActionCount = (this.specialSkillActions != null) ? this.specialSkillActions.size() : 0;
        this.stressActionsCount = (this.stressActions != null) ? this.stressActions.size() : 0;
    }
}
