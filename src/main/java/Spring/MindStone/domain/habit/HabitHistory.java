package Spring.MindStone.domain.habit;

import Spring.MindStone.domain.common.BaseEntity;


import Spring.MindStone.domain.enums.HabitStatus;
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
public class HabitHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit; // 회원 ID (FK, Member와 관계 설정)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private HabitStatus status; // 습관 상태 (Enum)
}
