package coollaafi.wot.web.post;

import coollaafi.wot.web.comment.CommentResponseDTO;
import coollaafi.wot.web.member.MemberDTO;
import coollaafi.wot.web.ootdImage.OotdImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateDTO {
        private Long postId;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO {
        private Long postId;
        private OotdImage ootdImage;
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
    public static class PostAddDTO {
        private String description;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllPostGetDTO {
        private MemberDTO.MemberBasedDTO member;
        private PostDTO post;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OnePostGetDTO {
        private MemberDTO.MemberBasedDTO member;
        private PostDTO post;
        private PostAddDTO postAdd;
        private List<CommentResponseDTO.CommentWithReplyDTO> comments;
    }
}
