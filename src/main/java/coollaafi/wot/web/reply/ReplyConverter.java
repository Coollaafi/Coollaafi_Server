package coollaafi.wot.web.reply;

import coollaafi.wot.web.comment.Comment;
import coollaafi.wot.web.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReplyConverter {
    public Reply toEntity(Comment comment, Member member, String content) {
        return Reply.builder()
                .comment(comment)
                .member(member)
                .content(content)
                .build();
    }

    public ReplyResponseDTO.ReplyCreateDTO toCreateDTO(Reply reply) {
        return ReplyResponseDTO.ReplyCreateDTO.builder()
                .replyId(reply.getId())
                .createdAt(reply.getCreatedAt())
                .build();
    }

    public List<ReplyResponseDTO.ReplyGetDTO> toGetDTO(List<Reply> replies) {
        return replies.stream()
                .map(reply -> new ReplyResponseDTO.ReplyGetDTO(
                        reply.getId(),
                        reply.getMember().getName(),
                        reply.getMember().getProfileimage(),
                        reply.getContent(),
                        reply.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
