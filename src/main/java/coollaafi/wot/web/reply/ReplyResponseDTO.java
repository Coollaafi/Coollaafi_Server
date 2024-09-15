package coollaafi.wot.web.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
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
        Long replyId;
        String memberName;
        URL memberImageUrl;
        String content;
        LocalDateTime createdAt;
    }
}
