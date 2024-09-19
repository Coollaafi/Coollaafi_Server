package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
//import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.web.friendship.FriendshipRepository;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.photo.PhotoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final MemberRepository memberRepository;
    private final AIService aiService;
    private final AmazonS3Manager amazonS3Manager;
    private final FriendshipRepository friendshipRepository;
    private final PhotoService photoService;

    @Transactional
    public PostResponseDTO.LookbookDTO createLookbook(Long memberId, MultipartFile ootdImage) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // ootd 이미지 S3에 업로드 후 Photo 엔티티에 저장
        String ootdUrl = photoService.savePhoto(ootdImage, "ootd/", memberId, member).getS3Url();
        System.out.println("ootd 성공");


        // lookbook 이미지 생성 후 S3에 업로드
        MultipartFile lookbookImage = aiService.generateLookBookImage(ootdImage);
        String lookbookUrl = amazonS3Manager.uploadFile("lookbook/", lookbookImage, memberId);

        return PostResponseDTO.LookbookDTO.builder()
                .ootdImage(ootdUrl)
                .lookbookImage(lookbookUrl)
                .build();
    }

    @Transactional
    public PostResponseDTO.PostCreateDTO firstCreatePost(Long memberId, String ootd, String lookbook) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Post post = postConverter.toEntity(ootd, lookbook, member);
        Post savedPost = postRepository.save(post);
        return postConverter.toPostCreateDTO(savedPost);
    }

    @Transactional
    public PostResponseDTO.PostCreateDTO createPost(PostRequestDTO.PostCreateRequestDTO requestDTO) {
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        post.setDescription(requestDTO.getDescription());
        post.setPostCondition(requestDTO.getPostCondition());
        Post savedPost = postRepository.save(post);
        return postConverter.toPostCreateDTO(savedPost);
    }

    @Transactional
    public List<PostResponseDTO.AllPostGetDTO> getAllPost(Long memberId) {
        // 멤버를 찾고, 친구 목록을 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));
        List<Member> friends = friendshipRepository.findFriendsOfMember(member);

        // 현재 시점에서 일주일 전 시간
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        // 친구들의 일주일 이내 게시글 조회
        List<Post> postList = postRepository.findAllByFriendsAndDate(friends, oneWeekAgo);

        return postConverter.toGetAllPostDTO(postList, member);
    }

    @Transactional
    public PostResponseDTO.OnePostGetDTO getPost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));

        return postConverter.toGetOnePostDTO(post, member);
    }
}
