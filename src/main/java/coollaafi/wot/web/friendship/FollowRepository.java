package coollaafi.wot.web.friendship;

import coollaafi.wot.web.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로워 목록 조회 (팔로우하는 사람들)
    @Query("SELECT f.follower FROM Follow f WHERE f.followee.id = :memberId")
    List<Member> findFollowersByMemberId(@Param("memberId") Long memberId);

    // 팔로우 목록 조회 (내가 팔로우하는 사람들)
    @Query("SELECT f.followee FROM Follow f WHERE f.follower.id = :memberId")
    List<Member> findFolloweesByMemberId(@Param("memberId") Long memberId);

    int deleteFollowByFollowerAndFollowee(Member follower, Member followee);

    Long countFollowsByFollowerId(Long followerId);

    boolean existsFollowByFollowerAndFollowee(Member follower, Member followee);
}
