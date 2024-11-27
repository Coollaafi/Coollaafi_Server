package coollaafi.wot.web.postPrefer;

import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPreferRepository extends JpaRepository<PostPrefer, Long> {
    boolean existsByMemberAndPost(Member member, Post post);

    Long countByPost(Post post);

    int deleteByPostIdAndMemberId(Long postId, Long memberId);
}
