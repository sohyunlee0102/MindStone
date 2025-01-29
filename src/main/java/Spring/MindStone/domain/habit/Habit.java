package Spring.MindStone.domain.habit;
import Spring.MindStone.domain.enums.HabitColor;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Habit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // 회원 ID와 연관 관계 설정
    private MemberInfo memberInfo;

    @Column(nullable = false, length = 50)
    private String title; // 제목

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;   //알람 활성화

    @Column(nullable = false)
    private String dayOfWeek;  //요일[0000000] -> [일월화수목금토]

    @Column(nullable = false)
    private String alarmTime;    //알람 울리는 시간, ex)15:00/16:00 매핑 방식으로 저장

    @Column(nullable = false)
    private Integer targetTime; //목표 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HabitColor habitColor; // 습관 별 색깔

}
