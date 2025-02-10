package Spring.MindStone.domain.diary;

import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@RequiredArgsConstructor
@AllArgsConstructor
/*@IdClass(DailyEmotionStatisticId.class) // 복합 키 클래스 매핑*/
public class DailyEmotionStatistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    //@Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // MemberInfo와 연관 관계
    private MemberInfo memberInfo;


    @Column(nullable = false)
    private LocalDate date; // 생성 날짜

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer angerFigure = 0; // 분노 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer depressionFigure = 0; // 우울 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer sadFigure = 0; // 슬픔 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer calmFigure = 0; // 슬픔 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer joyFigure = 0; // 기쁨 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer thrillFigure = 0; // 전율 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer happinessFigure = 0; // 행복 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 3")
    private Integer diaryAutoCreationCount = 3; // 일기자동생성횟수

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 10")
    private Integer actionRecommandCount =10; // 행동추천횟수

    public void updateEmotion(EmotionList emotion, int value) {
        switch (emotion) {
            case ANGER -> this.angerFigure += value;
            case DEPRESSION -> this.depressionFigure += value;
            case SAD -> this.sadFigure += value;
            case CALM -> this.calmFigure += value;
            case JOY -> this.joyFigure += value;
            case THRILL -> this.thrillFigure += value;
            case HAPPINESS -> this.happinessFigure += value;
        }
    }

    public DailyEmotionStatistic(MemberInfo memberInfo, LocalDate date){
        this.memberInfo = memberInfo;
        this.date = date;
    }
}
