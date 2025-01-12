package Spring.MindStone.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class FriendList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 친구 목록 ID (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo member; // 본인 (Member 테이블의 ID 참조)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = true)
    private MemberInfo friend; // 친구 (Member 테이블의 ID 참조)

    @Column(nullable = true)
    private Boolean isBan; // 차단 여부 (true: 차단, false: 차단 아님)
}
