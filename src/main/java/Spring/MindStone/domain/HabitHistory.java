package Spring.MindStone.domain;

import Spring.MindStone.domain.common.BaseEntity;


import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.HabitStatus;
import Spring.MindStone.domain.enums.MessageStatus;
import Spring.MindStone.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(nullable = false, length = 15, columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private HabitStatus status; // 계정 상태 (Enum)
}
