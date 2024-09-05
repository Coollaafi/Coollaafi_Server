package coollaafi.wot.web.postPrefer;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.post.PostHandler;
import coollaafi.wot.web.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PostPreferConverter {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    public PostPrefer toEntity(PostPreferRequestDTO request){
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return PostPrefer.builder()
                .post(post)
                .member(member)
                .build();
    }

    public static PostPreferResponseDTO toResultDTO(PostPrefer postPrefer){
        return PostPreferResponseDTO.builder()
                .postPreferId(postPrefer.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
