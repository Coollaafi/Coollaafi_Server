package coollaafi.wot.web.friendship;

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
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private Member followee;
}
