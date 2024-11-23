package coollaafi.wot.web.post;

import coollaafi.wot.web.comment.CommentRepository;
import coollaafi.wot.web.comment.CommentResponseDTO;
import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.ootdImage.OotdImage;
import coollaafi.wot.web.post.PostRequestDTO.PostCreateRequestDTO;
import coollaafi.wot.web.postPrefer.PostPreferRepository;
import coollaafi.wot.web.reply.ReplyRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final PostPreferRepository postPreferRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberConverter memberConverter;

    public Post toEntity(PostCreateRequestDTO postCreateRequestDTO, Member member, OotdImage ootdImage,
                         String lookbookUrl) {
        return Post.builder()
                .member(member)
                .ootdImage(ootdImage)
                .lookbookImage(lookbookUrl)
                .description(postCreateRequestDTO.getDescription())
                .postCondition(postCreateRequestDTO.getPostCondition())
                .build();
    }

    public PostResponseDTO.PostCreateDTO toPostCreateDTO(Post post) {
        return PostResponseDTO.PostCreateDTO.builder()
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public PostResponseDTO.PostDTO toPostDTO(Post post, Member member) {
        return PostResponseDTO.PostDTO.builder()
                .postId(post.getId())
                .ootd_url(post.getOotdImage().getS3url())
                .lookbook_url(post.getLookbookImage())
                .postCondition(post.getPostCondition())
                .address(extractCityAndDistrict(post.getOotdImage().getAddress()))
                .tmax(post.getOotdImage().getTmax())
                .tmin(post.getOotdImage().getTmin())
                .createdAt(post.getCreatedAt())
                .preferCount(postPreferRepository.countByPost(post))
                .commentCount(commentRepository.countCommentsByPost(post) + replyRepository.countRepliesByPost(post))
                .isLikedByMember(postPreferRepository.existsByPostAndMember(post, member))
                .build();
    }

    public PostResponseDTO.PostAddDTO toPostAddDTO(Post post) {
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


    public PostResponseDTO.OnePostGetDTO toGetOnePostDTO(Post post, Member member,
                                                         List<CommentResponseDTO.CommentWithReplyDTO> comments) {
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
            CalendarDTO.Day day = new CalendarDTO.Day(post.getCreatedAt().toLocalDate(), post.getLookbookImage(),
                    post.getId());  // Day 객체 생성
            calendarDTO.addDay(day);
        }

        return calendarDTO;
    }


    public String extractCityAndDistrict(String fullAddress) {
        String[] addressParts = fullAddress.split(" ");
        if (addressParts.length >= 2) {
            return addressParts[0] + " " + addressParts[1];  // 시/도 + 시/구 까지 반환
        }
        return fullAddress;  // 주소 형식이 다를 경우 전체 반환
    }
}
