package coollaafi.wot.web.friendship;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // 특정 멤버의 친구 목록을 가져오는 쿼리
    @Query("SELECT f.member1 FROM Friendship f WHERE f.member2 = :member UNION " +
            "SELECT f.member2 FROM Friendship f WHERE f.member1 = :member")
    List<Member> findFriendsOfMember(@Param("member") Member member);
}
