package coollaafi.wot.web.ootdImage;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OotdImageResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetadataDTO {
        private String address;
        private LocalDateTime date;
        private String description;
        private String imageUrl;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class uploadOOTD {
        private Long ootdImageId;
        private List<String> collageImages;
    }
}
