package Spring.MindStone.domain;


import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import Spring.MindStone.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private EmotionList emotion; // 감정 (Enum)

    /*@Column(nullable = false)
    private Boolean emotionValence; //+감정인지 -감정인지*/

    @Column(nullable = false)
    private Integer emotionFigure; // 감정 수치

    @Column(nullable = false, length = 200)
    private String content; // 상세 내용 (왜 기쁘거나 슬펐는지)

    @Override
    public String toString() {
        return content +"," + emotion +"," + emotionFigure;
    }
    /*return getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            + "-" + "Action : " + content + ",Emotion :" + emotion + ",Emotion Figure : " + emotionFigure;*/
}
