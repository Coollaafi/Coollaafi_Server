package coollaafi.wot.web.friendRequest;

import coollaafi.wot.web.member.converter.MemberConverter;
import coollaafi.wot.web.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendRequestConverter {
    private final MemberConverter memberConverter;

    public FriendRequest toEntity(Member sender, Member receiver) {
        return FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequest.RequestStatus.PENDING)
                .build();
    }

    public FriendRequestDTO.responseDTO toResult(FriendRequest friendRequest) {
        return FriendRequestDTO.responseDTO.builder()
                .id(friendRequest.getId())
                .created_at(friendRequest.getCreatedAt())
                .build();
    }

    public FriendRequestDTO.responseGetDTO toGetReceiverResult(FriendRequest friendRequest) {
        return FriendRequestDTO.responseGetDTO.builder()
                .id(friendRequest.getId())
                .status(friendRequest.getStatus())
                .created_at(friendRequest.getCreatedAt())
                .memberInfo(memberConverter.toMemberBasedDTO(friendRequest.getReceiver()))
                .build();
    }

    public FriendRequestDTO.responseGetDTO toGetSenderResult(FriendRequest friendRequest) {
        return FriendRequestDTO.responseGetDTO.builder()
                .id(friendRequest.getId())
                .status(friendRequest.getStatus())
                .created_at(friendRequest.getCreatedAt())
                .memberInfo(memberConverter.toMemberBasedDTO(friendRequest.getSender()))
                .build();
    }
}