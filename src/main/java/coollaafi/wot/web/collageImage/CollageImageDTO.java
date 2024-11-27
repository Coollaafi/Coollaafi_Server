package coollaafi.wot.web.collageImage;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CollageImageDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationRequireDTO {
        private Long memberId;
        private Double longitude;
        private Double latitude;
    }
}
