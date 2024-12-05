package coollaafi.wot.web.friendship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FriendshipDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class responseDTO{
        private Long id;
        private LocalDateTime created_at;
    }
}
