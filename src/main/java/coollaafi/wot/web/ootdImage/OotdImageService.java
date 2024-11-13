package coollaafi.wot.web.ootdImage;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.collageImage.CollageImageService;
import coollaafi.wot.web.collageImage.WeatherData;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.member.service.MemberService;
import coollaafi.wot.web.ootdImage.OotdImageResponseDTO.MetadataDTO;
import coollaafi.wot.web.ootdImage.OotdImageResponseDTO.uploadOOTD;
import coollaafi.wot.web.post.Category;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OotdImageService {
    private final OotdImageRepository ootdImageRepository;
    private final MemberService memberService;
    private final CollageImageService collageImageService;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final MetadataExtractor metadataExtractor;

    @Transactional
    public OotdImageResponseDTO.uploadOOTD segmentImage(Long memberId, MultipartFile ootdImage,
                                                        Set<Category> categorySet)
            throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (ootdImage.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
        }

        MetadataDTO metadata = metadataExtractor.extract(ootdImage);

        if (metadata == null) {
            throw new RuntimeException("메타데이터 추출 실패");
        }

        String ootdImageUrl = amazonS3Manager.uploadFile("/ootd", ootdImage, member.getKakaoId());

        // DB에 이미지와 날씨 정보 저장
        WeatherData weatherData = new WeatherData();
        saveOotdImage(member, weatherData, ootdImageUrl, metadata);

        List<String> collageImagesUrl = new ArrayList<>();
        for (String url : collageImagesUrl) {
            collageImageService.saveCollageImage(member, url, weatherData);
        }

        return new uploadOOTD(ootdImageUrl, collageImagesUrl);
    }

    @Transactional
    public OotdImage saveOotdImage(Member member, WeatherData weatherData, String s3url, MetadataDTO metadata) {
        // DB에 사진 정보와 메타데이터 저장
        OotdImage ootdImage = OotdImage.builder()
                .s3url(s3url)
                .member(member)
                .tmin(weatherData.getTmin())
                .tmax(weatherData.getTmax())
                .address(metadata.getAddress())
                .date(metadata.getDate())
                .build();

        memberService.setAlias(member);
        return ootdImageRepository.save(ootdImage);
    }
}
