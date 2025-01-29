package Spring.MindStone.domain.diary;

import Spring.MindStone.domain.enums.EmotionList;
import Spring.MindStone.domain.member.MemberInfo;
import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.member.MemberInterest;
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
@Setter
public class DailyDiary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false)
    private LocalDate date; // 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo memberInfo; // 본인 (Member 테이블의 ID 참조)

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private EmotionList emotion; // 감정 (Enum)

    @Column(nullable = false, length = 100)
    private String impressiveThing; // 인상 깊은 일

    @Column(nullable = false, length = 50)
    private String title; // 일기 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 일기 내용

    /*@Column(nullable = false)
    private Boolean isPublic; // 공개 여부*/

    @Column(nullable = true, length = 255)
    private String imagePath; // 이미지 경로

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> diaryImageList = new ArrayList<>();

    public void update(){

    }
}
