package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendRequest.FriendRequestHandler;
import coollaafi.wot.web.friendRequest.FriendRequestRepository;
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
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipConverter friendshipConverter;
    private final MemberRepository memberRepository;

    @Transactional
    public FriendshipDTO.responseDTO acceptFriendRequest(Long id) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_NOT_FOUND));
        if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
            throw new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_ALREADY_PROCESSED);
        }
        Friendship friendship = friendshipConverter.toEntity(request);
        friendshipRepository.save(friendship);

        // Update request status
        request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        return friendshipConverter.toResult(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long id) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_NOT_FOUND));
        if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
            throw new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_ALREADY_PROCESSED);
        }

        // Update request status
        request.setStatus(FriendRequest.RequestStatus.REJECTED);
        friendRequestRepository.save(request);
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
        FriendRequest friendRequest1 = friendRequestRepository.findBySenderAndReceiver(member1, member2);
        FriendRequest friendRequest2 = friendRequestRepository.findBySenderAndReceiver(member2, member1);
        friendshipRepository.delete(friendship);
        friendRequestRepository.delete(friendRequest1);
        friendRequestRepository.delete(friendRequest2);
    }
}
