package coollaafi.wot.web.photo;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final PhotoRepository photoRepository;

    // 여러 사진을 업로드하고 메타데이터 저장
    @Transactional
    public void uploadPhotos(List<MultipartFile> photos, Long memberId) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        for (MultipartFile file : photos) {
            savePhoto(file, "ootd/", memberId, member);
        }
    }

    // S3에 파일 업로드 및 메타데이터 추출해 Photo 엔티티로 추가
    @Transactional
    public Photo savePhoto(MultipartFile file, String keyName, Long memberId, Member member) throws IOException {
        // S3에 파일 업로드
        String s3Url = amazonS3Manager.uploadFile(keyName, file, memberId);

        // 메타데이터 추출
        PhotoResponseDTO.PhotoMetadata metadata = MetadataExtractor.extract(file);

        // DB에 사진 정보와 메타데이터 저장
        Photo photo = new Photo();
        photo.setS3Url(s3Url);
        photo.setLatitude(metadata.getLatitude());
        photo.setLongitude(metadata.getLongitude());
        photo.setPhotoDate(metadata.getDate());
        photo.setUploadDate(new Date());
        photo.setMember(member);

        return photoRepository.save(photo);  // 사진과 메타데이터 저장
    }
}
