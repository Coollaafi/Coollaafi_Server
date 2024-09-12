package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
//import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.web.friendship.FriendshipRepository;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final MemberRepository memberRepository;
    private final AIService aiService;
    private final AmazonS3Manager amazonS3Manager;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public PostResponseDTO.PostCreateLookBookResultDTO createPostLookbook(Long memberId, MultipartFile ootdImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        String ootdImageUrl = amazonS3Manager.uploadFile("postOotd/" + UUID.randomUUID().toString(), ootdImage);
        MultipartFile lookbookImage = aiService.generateLookBookImage(ootdImage);
        String lookbookImageUrl = amazonS3Manager.uploadFile("postLookbook/" + UUID.randomUUID().toString(), lookbookImage);

        Post post = postConverter.toEntity(ootdImageUrl, lookbookImageUrl, member);
        Post savedPost = postRepository.save(post);
        return postConverter.toCreateOotdResultDTO(savedPost);
    }

    @Transactional
    public PostResponseDTO.PostCreateResultDTO createPost(PostRequestDTO.PostCreateRequestDTO requestDTO) {
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        post.setDescription(requestDTO.getDescription());
        post.setPostCondition(requestDTO.getPostCondition());
        Post savedPost = postRepository.save(post);
        return postConverter.toCreateResultDTO(savedPost);
    }

    @Transactional
    public List<PostResponseDTO.PostGetResultDTO> getPost(Long memberId) {
        // 멤버를 찾고, 친구 목록을 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));
        List<Member> friends = friendshipRepository.findFriendsOfMember(member);

        // 현재 시점에서 일주일 전 시간
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        // 친구들의 일주일 이내 게시글 조회
        List<Post> postList = postRepository.findAllByFriendsAndDate(friends, oneWeekAgo);

        return postConverter.toGetResultDTO(postList, member);
    }
}
