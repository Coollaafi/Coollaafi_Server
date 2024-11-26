package coollaafi.wot.web.friendship;

import coollaafi.wot.web.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowConverter {
    public Follow toEntity(Member follower, Member followee) {
        return Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
    }

    public FollowDTO.responseDTO toResult(Follow follow) {
        return FollowDTO.responseDTO.builder()
                .id(follow.getId())
                .created_at(follow.getCreatedAt())
                .build();
    }
}
