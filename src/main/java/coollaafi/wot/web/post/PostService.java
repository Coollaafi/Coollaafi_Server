package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.comment.CommentResponseDTO;
import coollaafi.wot.web.comment.CommentService;
import coollaafi.wot.web.friendship.FriendshipRepository;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.member.service.MemberService;
import coollaafi.wot.web.ootdImage.OotdImage;
import coollaafi.wot.web.ootdImage.OotdImageHandler;
import coollaafi.wot.web.ootdImage.OotdImageRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final MemberRepository memberRepository;
    private final FriendshipRepository friendshipRepository;
    private final CommentService commentService;
    private final MemberService memberService;
    private final AmazonS3Manager amazonS3Manager;
    private final OotdImageRepository ootdImageRepository;

    @Transactional
    public PostResponseDTO.PostCreateDTO createPost(PostRequestDTO.PostCreateRequestDTO requestDTO,
                                                    MultipartFile lookbookImage)
            throws IOException {
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        OotdImage ootdImage = ootdImageRepository.findById(requestDTO.getOotdImageId())
                .orElseThrow(() -> new OotdImageHandler(ErrorStatus.OOTD_NOT_FOUND));

        String lookbookUrl = amazonS3Manager.uploadFile("/lookbook", lookbookImage, member.getKakaoId());

        Post post = postConverter.toEntity(requestDTO, member, ootdImage, lookbookUrl);
        postRepository.save(post);
        memberService.setAlias(member);
        return postConverter.toPostCreateDTO(post);
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
    public List<PostResponseDTO.AllPostGetDTO> getPostsByLocation(Long memberId, String address) {
        // 멤버와 친구 목록 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<Member> friends = friendshipRepository.findFriendsOfMember(member);

        // 현재 시점에서 일주일 전 시간
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        // 입력된 주소를 시/도 + 시/구 형식으로 변환
        String cityAndDistrict = postConverter.extractCityAndDistrict(address);

        // 친구들의 일주일 이내 게시글 중 주소와 일치하는 게시글 조회
        List<Post> postList = postRepository.findAllByFriendsAndDateAndAddress(friends, oneWeekAgo, cityAndDistrict);

        return postConverter.toGetAllPostDTO(postList, member);
    }


    @Transactional
    public PostResponseDTO.OnePostGetDTO getPost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler((ErrorStatus.MEMBER_NOT_FOUND)));
        List<CommentResponseDTO.CommentWithReplyDTO> comments = commentService.getCommentsByPostId(postId);
        return postConverter.toGetOnePostDTO(post, member, comments);
    }

    @Transactional
    public CalendarDTO getCalendar(Long memberId, Integer year, Integer month) {
        // 1. year와 month가 null일 경우 현재 날짜 기준으로 설정
        LocalDate currentDate = LocalDate.now();
        year = year == null ? currentDate.getYear() : year;
        month = month == null ? currentDate.getMonthValue() : month;

        // 2. 해당 연도/월의 첫날과 마지막 날 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 3. LocalDate -> LocalDateTime 변환
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59

        // 4. memberId로 해당 기간에 해당하는 게시물 조회
        List<Post> posts = postRepository.findPostsByMemberAndDateRange(memberId, startDateTime, endDateTime);

        // 5. 캘린더를 생성하여 반환
        return postConverter.createCalendarDTO(posts);
    }
}
