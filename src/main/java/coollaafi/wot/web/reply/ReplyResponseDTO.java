package coollaafi.wot.web.reply;

import coollaafi.wot.web.member.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReplyResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyCreateDTO{
        Long replyId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyGetDTO{
        MemberDTO.MemberBasedDTO member;
        Long replyId;
        String content;
        LocalDateTime createdAt;
    }
}
