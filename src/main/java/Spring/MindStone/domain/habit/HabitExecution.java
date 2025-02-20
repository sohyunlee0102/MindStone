package Spring.MindStone.domain.habit;

import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.member.MemberInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class HabitExecution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private LocalDateTime startTime;

    @Column(nullable = true)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_history_id", nullable = false)
    private HabitHistory habitHistory;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberInfo memberInfo;

}
