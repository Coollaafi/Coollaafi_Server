package coollaafi.wot.web.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateRequestDTO {
        private Long memberId;
        private Long ootdImageId;
        private String description;
        private PostCondition postCondition;
    }
}
