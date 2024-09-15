package coollaafi.wot.web.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

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
        Long commentId;
        String memberName;
        URL memberImageUrl;
        String content;
        Long replyCount;
        LocalDateTime createdAt;
    }
}
