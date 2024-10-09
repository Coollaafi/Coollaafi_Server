package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.comment.CommentRepository;
import coollaafi.wot.web.comment.CommentResponseDTO;
import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.postPrefer.PostPreferRepository;
import coollaafi.wot.web.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final PostPreferRepository postPreferRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberConverter memberConverter;
    private final PostRepository postRepository;

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
                        memberConverter.toMemberBasedDTO(post.getMember()),
                        toPostDTO(post, member)))
                .collect(Collectors.toList());
    }


    public PostResponseDTO.OnePostGetDTO toGetOnePostDTO(Post post, Member member, List<CommentResponseDTO.CommentWithReplyDTO> comments) {
        return PostResponseDTO.OnePostGetDTO.builder()
                .member(memberConverter.toMemberBasedDTO(post.getMember()))
                .post(toPostDTO(post, member))
                .postAdd(toPostAddDTO(post))
                .comments(comments)
                .build();
    }

    public CalendarDTO createCalendarDTO(List<Post> posts) {
        CalendarDTO calendarDTO = new CalendarDTO();

        for (Post post : posts) {
            CalendarDTO.Day day = new CalendarDTO.Day(post.getCreatedAt().toLocalDate(), post.getLookbookImage(), post.getId());  // Day 객체 생성
            calendarDTO.addDay(day);
        }

        return calendarDTO;
    }
}
