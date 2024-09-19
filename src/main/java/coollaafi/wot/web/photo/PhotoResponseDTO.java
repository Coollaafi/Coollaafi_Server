package coollaafi.wot.web.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class PhotoResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoMetadata{
        private Double latitude;
        private Double longitude;
        private Date date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoResponse{
        private String fileDownloadUri;
        private PhotoMetadata metadata;
    }
}
