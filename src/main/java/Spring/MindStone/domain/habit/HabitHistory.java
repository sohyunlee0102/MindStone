package Spring.MindStone.domain.habit;

import Spring.MindStone.domain.common.BaseEntity;


import Spring.MindStone.domain.enums.HabitColor;
import Spring.MindStone.domain.enums.HabitStatus;
import Spring.MindStone.domain.member.MemberInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HabitHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private HabitColor habitHistoryColor; // 습관 기록 별 색깔

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "habitHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitExecution> executions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit; // 회원 ID (FK, Member와 관계 설정)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo memberInfo; // 회원 ID (FK, Member와 관계 설정)

}
