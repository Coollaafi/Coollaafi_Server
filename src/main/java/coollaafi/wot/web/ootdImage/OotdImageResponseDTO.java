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
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class uploadOOTD {
        private String ootdImage;
        private List<String> collageImages;
    }
}
