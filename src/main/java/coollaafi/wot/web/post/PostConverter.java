package coollaafi.wot.web.post;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {
    public Post toEntity(String ootdImage, String lookbookImage, Member member) {
        return Post.builder()
                .member(member)
                .ootdImage(ootdImage)
                .lookbookImage(lookbookImage)
                .build();
    }

    public PostResponseDTO.PostCreateLookBookResultDTO toCreateOotdResultDTO(Post post) {
        return PostResponseDTO.PostCreateLookBookResultDTO.builder()
                .postId(post.getId())
                .lookbookImage(post.getLookbookImage())
                .ootdImage(post.getOotdImage())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public PostResponseDTO.PostCreateResultDTO toCreateResultDTO(Post post) {
        return PostResponseDTO.PostCreateResultDTO.builder()
                .postId(post.getId())
                .lookbookImage(post.getLookbookImage())
                .ootdImage(post.getOotdImage())
                .description(post.getDescription())
                .postCondition(post.getPostCondition())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
