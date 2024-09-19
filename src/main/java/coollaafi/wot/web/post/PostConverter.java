package coollaafi.wot.web.post;

import coollaafi.wot.web.comment.CommentRepository;
import coollaafi.wot.web.member.MemberDTO;
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

    public PostResponseDTO.PostCreateDTO toPostCreateDTO(Post post) {
        return PostResponseDTO.PostCreateDTO.builder()
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public MemberDTO toMemberDTO(Member member){
        return MemberDTO.builder()
                .memberName(member.getName())
                .memberImage(member.getProfileimage())
                .build();
    }

    public PostResponseDTO.PostDTO toPostDTO(Post post, Member member){
        return PostResponseDTO.PostDTO.builder()
                .postId(post.getId())
                .ootdImage(post.getOotdImage())
                .lookbookImage(post.getLookbookImage())
                .postCondition(post.getPostCondition())
                .createdAt(post.getCreatedAt())
                .preferCount(postPreferRepository.countByPost(post))
                .commentCount(commentRepository.countCommentsByPost(post) + replyRepository.countRepliesByPost(post))
                .isLikedByMember(postPreferRepository.existsByPostAndMember(post, member))
                .build();
    }

    public PostResponseDTO.PostAddDTO toPostAddDTO(Post post){
        return PostResponseDTO.PostAddDTO.builder()
                .description(post.getDescription())
                .build();
    }

    public List<PostResponseDTO.AllPostGetDTO> toGetAllPostDTO(List<Post> posts, Member member) {
        return posts.stream()
                .map(post -> new PostResponseDTO.AllPostGetDTO(
                        toMemberDTO(post.getMember()),
                        toPostDTO(post, member)))
                .collect(Collectors.toList());
    }


    public PostResponseDTO.OnePostGetDTO toGetOnePostDTO(Post post, Member member) {
        return PostResponseDTO.OnePostGetDTO.builder()
                .member(toMemberDTO(post.getMember()))
                .post(toPostDTO(post, member))
                .postAdd(toPostAddDTO(post))
                .build();
    }
}
