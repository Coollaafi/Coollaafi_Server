package coollaafi.wot.web.comment;

import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.post.PostResponseDTO;
import coollaafi.wot.web.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentConverter {
    private final ReplyRepository replyRepository;
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

    public List<CommentResponseDTO.CommentGetDTO> toGetDTO(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new CommentResponseDTO.CommentGetDTO(
                        comment.getId(),
                        comment.getMember().getName(),
                        comment.getMember().getProfileimage(),
                        comment.getContent(),
                        replyRepository.countByComment(comment),
                        comment.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
