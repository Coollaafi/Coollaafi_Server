package coollaafi.wot.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import coollaafi.wot.config.AmazonConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file, Long kakaoId)
            throws IOException {
        String fileName = keyName + kakaoId + "/" + UUID.randomUUID();

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream fileStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), fileName, fileStream, metadata));
            return amazonS3.getUrl(amazonConfig.getBucket(), fileName).toString();
        } catch (IOException e) {
            log.error("S3 업로드 실패: {}", e.getMessage());
            throw new IOException("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}