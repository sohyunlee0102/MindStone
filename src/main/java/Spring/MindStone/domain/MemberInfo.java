package Spring.MindStone.domain;

import Spring.MindStone.domain.common.BaseEntity;
import Spring.MindStone.domain.enums.Job;
import Spring.MindStone.domain.enums.MBTI;
import Spring.MindStone.domain.enums.Role;
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
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원 ID

    @Column(nullable = false, unique = true, length = 50)
    private String email; // 이메일

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(nullable = false, length = 30)
    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private MBTI mbti; // MBTI (Enum)

    @Column(nullable = false)
    private LocalDate birthday; // 출생 연도

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private Job job; // 직업 (Enum)

    @Column(nullable = true)
    private LocalDate inactiveDate; // 마지막 접속 기록

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private Status status; // 계정 상태 (Enum)

    @Column(nullable = false)
    private Boolean shareScope; // 공유 범위 (우선 boolean으로)

    @Column(nullable = false)
    private Boolean marketingAgree; // 서비스 동의 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, columnDefinition = "VARCHAR(15) DEFAULT 'USER'")
    private Role role;

    @OneToMany(mappedBy = "memberInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterest> memberInterestList = new ArrayList<>();

    @OneToMany(mappedBy = "memberInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyDiary> dailyDiaryList = new ArrayList<>();

    @OneToMany(mappedBy = "memberInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habit> habitList = new ArrayList<>();

    @OneToOne(mappedBy = "memberInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "memberInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmotionNote> emotionNoteList = new ArrayList<>();

    public void encodePassword(String password) { this.password = password; }
}
