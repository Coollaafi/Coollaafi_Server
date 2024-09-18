package coollaafi.wot.web.post;

import coollaafi.wot.web.member.MemberDTO;
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
    public static class LookbookDTO{
        private String ootdImage;
        private String lookbookImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateDTO{
        private Long postId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO{
        private Long postId;
        private String ootdImage;
        private String lookbookImage;
        private PostCondition postCondition;
        private LocalDateTime createdAt;
        private Long preferCount;
        private Long commentCount;
        private Boolean isLikedByMember;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAddDTO{
        private String description;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllPostGetDTO{
        private MemberDTO member;
        private PostDTO post;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OnePostGetDTO{
        private MemberDTO member;
        private PostDTO post;
        private PostAddDTO postAdd;
    }
}
