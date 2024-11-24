package coollaafi.wot.web.ootdImage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import coollaafi.wot.web.ootdImage.OotdImageResponseDTO.MetadataDTO;
import coollaafi.wot.web.post.WeatherResponse;
import coollaafi.wot.web.post.WeatherService;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class MetadataExtractor {
    private final KakaoAddressService kakaoAddressService;
    private final WeatherService weatherService;

    public MetadataDTO extract(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            // 메타데이터 추출
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            // 촬영 날짜 추출
            LocalDateTime captureDate = extractCaptureDate(metadata);

            // 위도, 경도 추출
            double latitude = extractLatitude(metadata);
            double longitude = extractLongitude(metadata);

            // 주소 검색 수행
            String address = kakaoAddressService.getAddressFromCoordinates(latitude, longitude);

            WeatherResponse weather_info = weatherService.getWeatherByCoordinates(latitude, longitude);

            return new MetadataDTO(address, captureDate, weather_info.getDescription(), weather_info.getIconUrl());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private LocalDateTime extractCaptureDate(Metadata metadata) {
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        String captureDateString =
                directory != null ? directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) : null;

        // 날짜가 null이면 현재 날짜와 시간을 기본값으로 설정
        if (captureDateString == null) {
            return LocalDateTime.now();
        } else {
            // 원본 날짜를 LocalDateTime 형식으로 변환
            DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
            return LocalDateTime.parse(captureDateString, originalFormat);
        }
    }

    private double extractLatitude(Metadata metadata) {
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        return gpsDirectory != null && gpsDirectory.getGeoLocation() != null
                ? gpsDirectory.getGeoLocation().getLatitude()
                : 37.57636667; // 기본값
    }

    private double extractLongitude(Metadata metadata) {
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        return gpsDirectory != null && gpsDirectory.getGeoLocation() != null
                ? gpsDirectory.getGeoLocation().getLongitude()
                : 126.9388972; // 기본값
    }
}
