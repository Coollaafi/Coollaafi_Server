package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.MemberDTO.MemberBasedDTO;
import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private static final int MAX_FOLLOW_LIMIT = 20; // 팔로우 제한 수
    private final FollowRepository followRepository;
    private final FollowConverter followConverter;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    @Transactional
    public FollowDTO.responseDTO followRequest(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new FollowHandler(ErrorStatus.FOLLOWER_EQUAL_FOLLOWEE);
        }

        Long followeeCount = followRepository.countFollowsByFollowerId(followerId);
        if (followeeCount >= MAX_FOLLOW_LIMIT) {
            throw new FollowHandler(ErrorStatus.LIMIT_FOLLOW);
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.FOLLOWER_NOT_FOUND));
        Member followee = memberRepository.findById(followeeId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.FOLLOWEE_NOT_FOUND));

        if (followRepository.existsFollowByFollowerAndFollowee(follower, followee)) {
            throw new FollowHandler(ErrorStatus.ALREADY_FOLLOWING);
        }

        Follow follow = followConverter.toEntity(follower, followee);
        followRepository.save(follow);

        return followConverter.toResult(follow);
    }

    @Transactional
    public void rejectFollowRequest(Long followerId, Long followeeId) {
        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.FOLLOWER_NOT_FOUND));
        Member followee = memberRepository.findById(followeeId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.FOLLOWEE_NOT_FOUND));

        int deletedCount = followRepository.deleteFollowByFollowerAndFollowee(follower, followee);
        if (deletedCount == 0) {
            throw new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND);
        }
    }

    public List<MemberBasedDTO> getFollowers(Long followerId) {
        List<Member> members = followRepository.findFollowersByMemberId(followerId);
        return members.stream()
                .map(memberConverter::toMemberBasedDTO).collect(Collectors.toList());
    }

    public List<MemberBasedDTO> getFollowees(Long followeeId) {
        List<Member> members = followRepository.findFolloweesByMemberId(followeeId);
        return members.stream()
                .map(memberConverter::toMemberBasedDTO).collect(Collectors.toList());
    }
}
