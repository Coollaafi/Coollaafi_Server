package coollaafi.wot.web.comment;

import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.reply.ReplyResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentCreateDTO{
        Long commentId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentGetDTO{
        MemberDTO.MemberBasedDTO member;
        Long commentId;
        String content;
        Long replyCount;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentWithReplyDTO{
        CommentGetDTO comment;
        List<ReplyResponseDTO.ReplyGetDTO> replies;
    }
}
