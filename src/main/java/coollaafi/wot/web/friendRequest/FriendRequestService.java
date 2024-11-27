package coollaafi.wot.web.friendRequest;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRequestConverter friendRequestConverter;

    @Transactional
    public FriendRequestDTO.responseDTO sendFriendRequest(Long senderId, Long receiverId){
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.SENDER_NOT_FOUND));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.RECEIVER_NOT_FOUND));

        if(friendRequestRepository.existsBySenderAndReceiver(sender, receiver)){
            throw new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_ALREADY_EXIST);
        }

        FriendRequest friendRequest = friendRequestConverter.toEntity(sender, receiver);
        friendRequestRepository.save(friendRequest);
        return friendRequestConverter.toResult(friendRequest);
    }
}
