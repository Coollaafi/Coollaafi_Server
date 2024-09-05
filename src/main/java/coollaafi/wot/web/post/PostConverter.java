package coollaafi.wot.web.post;

import coollaafi.wot.web.member.entity.Member;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class PostConverter {
    public Post toEntity(URL ootdImage, URL lookbookImage, Member member) {
        return Post.builder()
                .member(member)
                .ootdImage(ootdImage)
                .lookbookImage(lookbookImage)
                .build();
    }

    public PostResponseDTO.PostCreateLookBookResultDTO toCreateOotdResultDTO(Post post) {
        return PostResponseDTO.PostCreateLookBookResultDTO.builder()
                .lookbookImage(post.getLookbookImage())
                .ootdImage(post.getOotdImage())
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
