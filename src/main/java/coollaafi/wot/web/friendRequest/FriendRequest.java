package coollaafi.wot.web.friendRequest;

import coollaafi.wot.common.BaseEntity;
import coollaafi.wot.web.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public enum RequestStatus{
        PENDING, ACCEPTED, REJECTED
    }
}
