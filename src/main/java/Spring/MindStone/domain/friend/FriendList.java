package Spring.MindStone.domain.friend;

import Spring.MindStone.domain.enums.FriendStatus;
import Spring.MindStone.domain.member.MemberInfo;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private FriendStatus friendStatus; // 친구 추가 상태(친추됨, 차단됨, 요청됨, 요청이 옴)
}
