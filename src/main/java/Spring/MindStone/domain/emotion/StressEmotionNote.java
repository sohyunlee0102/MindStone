package Spring.MindStone.domain.emotion;

import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StressEmotionNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // 회원 ID와 연관 관계
    private MemberInfo memberInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private EmotionList emotion; // 감정 (Enum)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stress_id",nullable = false)
    private EmotionNote stressNote;

    @Column(nullable = false)
    private Integer emotionFigure; // 감정 수치

    @Column(nullable = false, length = 200)
    private String content; // 상세 내용 (왜 기쁘거나 슬펐는지)

    @Override
    public String toString() {
        return "그때문에 " +content +"," + emotion +"," + emotionFigure;
    }
}
