package coollaafi.wot.web.postPrefer;

import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPreferRepository extends JpaRepository<PostPrefer, Long> {
    List<PostPrefer> findByMember(Member member);

    // 사용자가 해당 게시글에 좋아요를 눌렀는지 여부
    @Query("SELECT COUNT(pp) > 0 FROM PostPrefer pp WHERE pp.post = :post AND pp.member = :member")
    boolean existsByPostAndMember(@Param("post") Post post, @Param("member") Member member);

    // 게시글에 대한 prefer 개수 조회
    @Query("SELECT COUNT(pp) FROM PostPrefer pp WHERE pp.post = :post")
    Long countByPost(@Param("post") Post post);
}
