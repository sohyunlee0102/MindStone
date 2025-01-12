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

    @Column(nullable = false)
    private LocalDateTime alarmTime;

}
