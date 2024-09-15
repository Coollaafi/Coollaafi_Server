package coollaafi.wot.web.reply;

import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // 특정 댓글에 달린 대댓글을 모두 조회
    List<Reply> findByComment(Comment comment);

    // 댓글에 대한 reply 개수 조회
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.comment = :comment")
    Long countByComment(@Param("comment") Comment comment);

    @Query("SELECT COUNT(r) FROM Reply r WHERE r.comment IN (SELECT c FROM Comment c WHERE c.post = :post)")
    Long countRepliesByPost(@Param("post") Post post);
}
