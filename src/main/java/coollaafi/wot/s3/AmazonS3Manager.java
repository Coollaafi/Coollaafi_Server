package coollaafi.wot.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import coollaafi.wot.config.AmazonConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file, Long memberId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 고유한 파일 이름 생성
        String fileName = keyName + memberId + "/" + UUID.randomUUID();

        // 파일 메타데이터 설정(크기, 파일 타입 등)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try{
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(),fileName,file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}",(Object) e.getStackTrace());
        }

        // 업로드된 파일 URL 반환
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
