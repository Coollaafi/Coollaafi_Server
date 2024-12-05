package coollaafi.wot.web.comment;

import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.reply.ReplyRepository;
import coollaafi.wot.web.reply.ReplyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentConverter {
    private final ReplyRepository replyRepository;
    private final MemberConverter memberConverter;

    public Comment toEntity(Post post, Member member, String content) {
        return Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();
    }

    public CommentResponseDTO.CommentCreateDTO toCreateDTO(Comment comment) {
        return CommentResponseDTO.CommentCreateDTO.builder()
                .commentId(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public CommentResponseDTO.CommentGetDTO toCommentGetDTO(Comment comment){
        return CommentResponseDTO.CommentGetDTO.builder()
                .member(memberConverter.toMemberBasedDTO(comment.getMember()))
                .commentId(comment.getId())
                .content(comment.getContent())
                .replyCount(replyRepository.countByComment(comment))
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public CommentResponseDTO.CommentWithReplyDTO toCommentWithRepliesDTO(Comment comment, List<ReplyResponseDTO.ReplyGetDTO> replies){
        return CommentResponseDTO.CommentWithReplyDTO.builder()
                .comment(toCommentGetDTO(comment))
                .replies(replies)
                .build();
    }
}
