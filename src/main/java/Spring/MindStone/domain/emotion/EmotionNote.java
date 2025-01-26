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
public class EmotionNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // 회원 ID와 연관 관계
    private MemberInfo memberInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private EmotionList emotion; // 감정 (Enum)

    /*@Column(nullable = false)
    private Boolean emotionValence; //+감정인지 -감정인지*/

    @Column(nullable = false)
    private Integer emotionFigure; // 감정 수치

    @Column(nullable = false, length = 200)
    private String content; // 상세 내용 (왜 기쁘거나 슬펐는지)
}
