package coollaafi.wot.web.friendRequest;

import coollaafi.wot.web.friendRequest.FriendRequest.RequestStatus;
import coollaafi.wot.web.member.MemberDTO.MemberBasedDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FriendRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class responseDTO {
        private Long id;
        private LocalDateTime created_at;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class responseGetDTO {
        private Long id;
        private LocalDateTime created_at;
        private RequestStatus status;
        private MemberBasedDTO memberInfo;
    }
}
