package coollaafi.wot.web.post;

import coollaafi.wot.web.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMemberInAndCreatedAtAfterOrderByCreatedAtDesc(List<Member> member, LocalDateTime createdAt);

    @Query("SELECT p FROM Post p JOIN p.ootdImage oi " +
            "WHERE p.member IN :friends " +
            "AND p.createdAt >= :oneWeekAgo " +
            "AND oi.address LIKE CONCAT(:cityAndDistrict, '%')" +
            "ORDER BY p.createdAt DESC")
    List<Post> findAllByFriendsAndDateAndAddress(@Param("friends") List<Member> friends,
                                                 @Param("oneWeekAgo") LocalDateTime oneWeekAgo,
                                                 @Param("cityAndDistrict") String cityAndDistrict);

    List<Post> findPostsByMemberId(Long memberId);
}
