package coollaafi.wot.web.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URL;

public class PostRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateLookBookRequestDTO{
        @NotNull
        private Long memberId;
        private URL ootdImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateRequestDTO{
        @NotNull
        private Long postId;
        private String description;
        private PostCondition postCondition;
    }
}
