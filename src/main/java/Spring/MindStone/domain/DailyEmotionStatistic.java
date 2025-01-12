package Spring.MindStone.domain;

import Spring.MindStone.domain.id.DailyEmotionStatisticId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(DailyEmotionStatisticId.class) // 복합 키 클래스 매핑
public class DailyEmotionStatistic {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) // MemberInfo와 연관 관계
    private MemberInfo memberInfo;

    @Id
    @Column(nullable = false)
    private LocalDate date; // 생성 날짜 (복합 키)

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer angerFigure; // 분노 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer depressionFigure; // 우울 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer sadFigure; // 슬픔 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer joyFigure; // 기쁨 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer thrillFigure; // 전율 수치

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer happinessFigure; // 행복 수치
}
