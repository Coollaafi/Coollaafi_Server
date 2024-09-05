package coollaafi.wot.web.postPrefer;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
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
    public PostPreferResponseDTO saveSpacePrefer(PostPreferRequestDTO request){
        if (!postRepository.existsById(request.getPostId())) {
            throw new PostHandler(ErrorStatus.POST_NOT_FOUND);
        }
        if (!memberRepository.existsById(request.getMemberId())) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        PostPrefer postPrefer = postPreferConverter.toEntity(request);
        PostPrefer savedPostPrefer = postPreferRepository.save(postPrefer);
        return postPreferConverter.toResultDTO(savedPostPrefer);
    }

    @Transactional
    public void deletePostPrefer(Long id) {
        if (!postPreferRepository.existsById(id)) {
            throw new PostPreferHandler(ErrorStatus.POST_PREFER_NOT_FOUND);
        }
        postPreferRepository.deleteById(id);
    }
}
