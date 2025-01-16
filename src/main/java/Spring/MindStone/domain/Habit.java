package Spring.MindStone.domain;
import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.MessageStatus;
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
public class Habit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // 회원 ID와 연관 관계 설정
    private MemberInfo memberInfo;

    @Column(nullable = false, length = 50)
    private String title; // 제목

    @Column(nullable = false)
    private Integer alarmInterval; // 알람 간격

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;   //알람 활성화

    @Column(nullable = false)
    private String dayOfWeek;  //요일[0000000] -> [일월화수목금토]

    @Column(nullable = false)
    private String alarmTime;    //알람 울리는 시간, ex)15:00/16:00 매핑 방식으로 저장

    @Column(nullable = false)
    private Integer targetTime; //목표 시간

}
