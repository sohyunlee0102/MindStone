package Spring.MindStone.domain;


import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import Spring.MindStone.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false) // 감정 ID와 연관 관계
    private Emotion emotion;

    @Column(nullable = false)
    private Integer emotionFigure; // 감정 수치

    @Column(nullable = false, length = 200)
    private String content; // 상세 내용 (왜 기쁘거나 슬펐는지)
}
