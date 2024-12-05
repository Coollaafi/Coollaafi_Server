package coollaafi.wot.web.friendship;

import coollaafi.wot.web.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // 특정 멤버의 친구 목록을 가져오는 쿼리
    @Query("SELECT f.member1 FROM Friendship f WHERE f.member2 = :member UNION " +
            "SELECT f.member2 FROM Friendship f WHERE f.member1 = :member")
    List<Member> findFriendsOfMember(@Param("member") Member member);

    @Query("SELECT f FROM Friendship f WHERE (f.member1 = :member1 AND f.member2 = :member2) " +
            "OR (f.member1 = :member2 AND f.member2 = :member1)")
    Friendship findFriendshipBetweenMembers(@Param("member1") Member member1, @Param("member2") Member member2);
}
