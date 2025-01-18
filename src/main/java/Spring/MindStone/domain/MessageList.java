package Spring.MindStone.domain;

import Spring.MindStone.domain.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class MessageList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메시지 ID (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = true)
    private MemberInfo receiver; // 수신자 (MemberInfo ID를 참조)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private MemberInfo sender; // 발신자 (MemberInfo ID를 참조)

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 255)
    private MessageStatus status; // 상태 (ENUM)

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content; // 메시지 내용
}
