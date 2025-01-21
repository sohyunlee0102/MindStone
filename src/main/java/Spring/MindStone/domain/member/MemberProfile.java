package Spring.MindStone.domain.member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProfile {

    @Id
    @Column(name = "member_id") // 외래 키와 기본 키로 사용
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // memberId를 MemberInfo의 ID와 공유
    @JoinColumn(name = "member_id") // 외래 키 매핑
    private MemberInfo memberInfo;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer customInfo;

    @Column(nullable = false, length = 100)
    private String introduce; // 자기소개

    @Column(nullable = true)
    private String imageUrl;

}
