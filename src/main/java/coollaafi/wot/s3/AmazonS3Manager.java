package coollaafi.wot.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import coollaafi.wot.config.AmazonConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {
    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String key, MultipartFile file, Long kakaoId) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("upload");
        File savedFile = saveMultipartFileToTempDir(file, tempDir);

        File fileToUpload;
        if (isHeicFile(file.getOriginalFilename())) {
            fileToUpload = convertHeicToJpg(savedFile, tempDir);
        } else {
            fileToUpload = savedFile;
        }

        String filename = generateFileName(key, kakaoId);
        String fileUrl = uploadToS3(fileToUpload, filename);
        deleteTempDirectory(tempDir);

        return fileUrl;
    }

    public static String generateFileName(String keyName, Long kakaoId) {
        String uniqueId = UUID.randomUUID().toString();
        return String.format("%s%s/%s.jpg", keyName, kakaoId, uniqueId);
    }

    private File saveMultipartFileToTempDir(MultipartFile file, Path tempDir) throws IOException {
        File savedFile = new File(tempDir.toFile(), file.getOriginalFilename());
        file.transferTo(savedFile);
        return savedFile;
    }

    private boolean isHeicFile(String filename) {
        return getFileExtension(filename).equalsIgnoreCase("heic");
    }

    private File convertHeicToJpg(File heicFile, Path tempDir) throws IOException, InterruptedException {
        File jpgFile = new File(tempDir.toFile(), getFileNameWithoutExtension(heicFile.getName()) + ".jpg");
        Process process = new ProcessBuilder("convert", heicFile.getAbsolutePath(), jpgFile.getAbsolutePath()).start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Failed to convert HEIC to JPG.");
        }
        return jpgFile;
    }

    private String uploadToS3(File file, String key) {
        amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), key, file));
        return amazonS3.getUrl(amazonConfig.getBucket(), key).toString();
    }

    private void deleteTempDirectory(Path tempDir) throws IOException {
        FileUtils.deleteDirectory(tempDir.toFile());
    }

    private String getFileExtension(String filename) {
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

    private String getFileNameWithoutExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    public void deleteImage(String fileUrl) {
        try {
            String splitStr = ".com/";
            String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), fileName));
        } catch (SdkClientException e) {
            log.error("Error deleting file from s3");
        }
    }


}
