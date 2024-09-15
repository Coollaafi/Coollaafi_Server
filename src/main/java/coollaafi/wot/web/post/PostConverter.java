package coollaafi.wot.web.post;

import coollaafi.wot.web.comment.CommentRepository;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.postPrefer.PostPreferRepository;
import coollaafi.wot.web.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final PostPreferRepository postPreferRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

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

    public List<PostResponseDTO.PostGetResultDTO> toGetResultDTO(List<Post> posts, Member member) {
        return posts.stream()
                .map(post -> new PostResponseDTO.PostGetResultDTO(
                        post.getMember().getName(),
                        post.getMember().getProfileimage(),
                        post.getId(),
                        post.getLookbookImage(),
                        post.getOotdImage(),
                        post.getDescription(),
                        post.getPostCondition(),
                        post.getCreatedAt(),
                        postPreferRepository.countByPost(post), // prefer 개수
                        postPreferRepository.existsByPostAndMember(post, member),
                        commentRepository.countCommentsByPost(post) + replyRepository.countRepliesByPost(post)))
                .collect(Collectors.toList());
    }
}
