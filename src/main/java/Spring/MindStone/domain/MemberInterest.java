package Spring.MindStone.domain;

import Spring.MindStone.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.lang.reflect.Member;
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
public class MemberInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo member; // 회원 ID (FK, Member와 관계 설정)

    @Column(nullable = false)
    private Integer actionID; // 행동 번호(취미, 특기, 스트레스 풀이)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String action; // 행동 (텍스트)

    @Column(nullable = false)
    private Integer actionNum; // 행동 횟수
}
