package coollaafi.wot.web.post;

import coollaafi.wot.web.comment.CommentResponseDTO;
import coollaafi.wot.web.member.MemberDTO;
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
        private String ootd_url;
        private String lookbook_url;
        private String address;
        private Float tmin;
        private Float tmax;
        private String weather_description;
        private String weather_icon_url;
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
