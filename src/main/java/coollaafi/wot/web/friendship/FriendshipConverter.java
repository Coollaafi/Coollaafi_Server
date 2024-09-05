package coollaafi.wot.web.friendship;

import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendRequest.FriendRequestDTO;
import coollaafi.wot.web.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendshipConverter {
    public Friendship toEntity(FriendRequest request) {
        return Friendship.builder()
                .member1(request.getSender())
                .member2(request.getReceiver())
                .build();
    }

    public FriendshipDTO.responseDTO toResult(Friendship friendship) {
        return FriendshipDTO.responseDTO.builder()
                .id(friendship.getId())
                .created_at(friendship.getCreatedAt())
                .build();
    }
}
