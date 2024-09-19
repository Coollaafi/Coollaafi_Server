package coollaafi.wot.web.comment;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.post.PostHandler;
import coollaafi.wot.web.post.PostRepository;
import coollaafi.wot.web.reply.Reply;
import coollaafi.wot.web.reply.ReplyConverter;
import coollaafi.wot.web.reply.ReplyRepository;
import coollaafi.wot.web.reply.ReplyResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentConverter commentConverter;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final ReplyConverter replyConverter;

    // 댓글 작성
    @Transactional
    public CommentResponseDTO.CommentCreateDTO createComment(Long postId, Long memberId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Comment comment = commentConverter.toEntity(post, member, content);
        Comment savedComment = commentRepository.save(comment);

        return commentConverter.toCreateDTO(savedComment);
    }

    // 특정 게시글에 달린 댓글 조회
    @Transactional
    public List<CommentResponseDTO.CommentWithReplyDTO> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        List<Comment> commentList = commentRepository.findByPost(post);
        return commentList.stream().map(comment -> {
            List<Reply> replyList = replyRepository.findByComment(comment);
            List<ReplyResponseDTO.ReplyGetDTO> replies = replyConverter.toGetDTO(replyList);
            return commentConverter.toCommentWithRepliesDTO(comment, replies);
        }).collect(Collectors.toList());
    }
}
