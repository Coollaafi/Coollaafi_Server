package coollaafi.wot.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import coollaafi.wot.config.AmazonConfig;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file, Long memberId) throws IOException {
        // 고유한 파일 이름 생성
        String fileName = keyName + "/" + memberId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 파일 메타데이터 설정(크기, 파일 타입 등)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        amazonS3.putObject(amazonConfig.getBucket(),fileName ,file.getInputStream(), metadata);

        return amazonS3.getUrl(amazonConfig.getBucket(), fileName).toString();
    }

    public void deleteImage(String fileUrl){
        try{
            String splitStr = ".com/";
            String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), fileName));
        }
        catch(SdkClientException e){
            log.error("Error deleting file from s3");
        }
    }


}
