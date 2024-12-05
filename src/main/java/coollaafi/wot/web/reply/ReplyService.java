package coollaafi.wot.web.reply;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.comment.CommentHandler;
import coollaafi.wot.web.comment.CommentRepository;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReplyConverter replyConverter;

    // 대댓글 작성
    @Transactional
    public ReplyResponseDTO.ReplyCreateDTO createReply(Long commentId, Long memberId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Reply reply = replyConverter.toEntity(comment, member, content);
        Reply savedReply = replyRepository.save(reply);

        return replyConverter.toCreateDTO(savedReply);
    }

    // 특정 댓글에 달린 대댓글 조회
    public List<ReplyResponseDTO.ReplyGetDTO> getRepliesByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
        List<Reply> replyList = replyRepository.findByComment(comment);
        return replyConverter.toGetDTO(replyList);
    }
}
