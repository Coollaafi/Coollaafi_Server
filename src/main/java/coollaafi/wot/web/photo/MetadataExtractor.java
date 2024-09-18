package coollaafi.wot.web.photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MetadataExtractor {

    // MultipartFile로부터 메타데이터 추출
    public static PhotoResponseDTO.PhotoMetadata extract(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile); // 임시 파일로 저장

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(tempFile);

            // GPS 정보 추출
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            Double latitude = gpsDirectory != null ? gpsDirectory.getGeoLocation().getLatitude() : null;
            Double longitude = gpsDirectory != null ? gpsDirectory.getGeoLocation().getLongitude() : null;

            // 날짜 정보 추출
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date date = exifDirectory != null ? exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) : null;

            return new PhotoResponseDTO.PhotoMetadata(latitude, longitude, date);
        } catch (Exception e) {
            throw new RuntimeException("메타데이터 추출 실패: " + file.getOriginalFilename(), e);
        } finally {
            // 임시 파일 삭제
            tempFile.delete();
        }
    }
}