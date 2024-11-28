package coollaafi.wot.web.friendRequest;

import coollaafi.wot.web.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiver(Member sender, Member receiver);

    List<FriendRequest> findBySender(Member sender);

    List<FriendRequest> findByReceiver(Member receiver);

    FriendRequest findBySenderAndReceiver(Member sender, Member receiver);
}
