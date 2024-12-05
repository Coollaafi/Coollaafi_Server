package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;
    private final FriendshipConverter friendshipConverter;

    @Transactional
    public FriendshipDTO.responseDTO acceptFriendRequest(FriendRequest request) {
        Friendship friendship = friendshipConverter.toEntity(request);
        friendshipRepository.save(friendship);

        return friendshipConverter.toResult(friendship);
    }

    public void deleteFriend(Long memberId1, Long memberId2) {
        Member member1 = memberRepository.findById(memberId1)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.SENDER_NOT_FOUND));
        Member member2 = memberRepository.findById(memberId2)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.RECEIVER_NOT_FOUND));

        Friendship friendship = friendshipRepository.findFriendshipBetweenMembers(member1, member2);
        if (friendship == null) {
            throw new IllegalArgumentException("해당 친구 관계를 찾을 수 없습니다.");
        }
        friendshipRepository.delete(friendship);
    }
}
