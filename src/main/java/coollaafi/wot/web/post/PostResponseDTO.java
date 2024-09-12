package coollaafi.wot.web.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateLookBookResultDTO{
        private Long postId;
        private String ootdImage;
        private String lookbookImage;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateResultDTO{
        private Long postId;
        private String ootdImage;
        private String lookbookImage;
        private String description;
        private PostCondition postCondition;
        private LocalDateTime createdAt;
    }
}
