package coollaafi.wot.web.post;

import java.net.URL;

public class PostConverter {
    public Post toEntity(PostRequestDTO.PostCreateLookBookRequestDTO requestDTO, URL lookbookImage) {
        return Post.builder()
                .memberId(requestDTO.getMemberId())
                .ootdImage(requestDTO.getOotdImage())
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
                .createdAt(post.getCreatedAt())
                .build();
    }
}
