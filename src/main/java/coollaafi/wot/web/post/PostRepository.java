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
    @Query("SELECT p FROM Post p WHERE p.member IN :friends AND p.createdAt >= :oneWeekAgo")
    List<Post> findAllByFriendsAndDate(
            @Param("friends") List<Member> friends,
            @Param("oneWeekAgo") LocalDateTime oneWeekAgo);

    @Query("SELECT p FROM Post p JOIN p.ootdImage oi " +
            "WHERE p.member IN :friends " +
            "AND p.createdAt >= :oneWeekAgo " +
            "AND oi.address LIKE CONCAT(:cityAndDistrict, '%')")
    List<Post> findAllByFriendsAndDateAndAddress(@Param("friends") List<Member> friends,
                                                 @Param("oneWeekAgo") LocalDateTime oneWeekAgo,
                                                 @Param("cityAndDistrict") String cityAndDistrict);


    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Post> findPostsByMemberAndDateRange(@Param("memberId") Long memberId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}
