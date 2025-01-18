package Spring.MindStone.domain;

import Spring.MindStone.domain.common.BaseEntity;

import java.awt.*;

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
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiaryImage extends BaseEntity {
    //일기에 들어가는 이미지 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private DailyDiary diary;   //이미지 들어가는 일기 id

    @Column(nullable = false)
    private String imagePath;   //이미지 경로

    private Integer imageOrder; // 이미지 순서
}
