package coollaafi.wot.web.postPrefer;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.post.Post;
import coollaafi.wot.web.post.PostHandler;
import coollaafi.wot.web.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostPreferService {
    private final PostPreferRepository postPreferRepository;
    private final PostPreferConverter postPreferConverter;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostPreferResponseDTO saveSpacePrefer(PostPreferRequestDTO request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        PostPrefer postPrefer = postPreferConverter.toEntity(post, member);
        PostPrefer savedPostPrefer = postPreferRepository.save(postPrefer);
        return postPreferConverter.toResultDTO(savedPostPrefer);
    }

    @Transactional
    public void deletePostPrefer(PostPreferRequestDTO request) {
        int deletedCount = postPreferRepository.deleteByPostIdAndMemberId(request.getPostId(), request.getMemberId());
        if (deletedCount == 0) {
            throw new PostPreferHandler(ErrorStatus.POST_PREFER_NOT_FOUND);
        }
    }
}
