package coollaafi.wot.web.comment;

import coollaafi.wot.web.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글에 달린 댓글을 모두 조회
    List<Comment> findByPost(Post post);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post")
    Long countCommentsByPost(@Param("post") Post post);
}
