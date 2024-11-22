package coollaafi.wot.web.ootdImage;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.s3.AmazonS3Manager;
import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.web.collageImage.CollageImageService;
import coollaafi.wot.web.weatherData.WeatherData;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.member.service.MemberService;
import coollaafi.wot.web.ootdImage.OotdImageResponseDTO.MetadataDTO;
import coollaafi.wot.web.post.Category;
import coollaafi.wot.web.weatherData.WeatherDataService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class OotdImageService {
    private final OotdImageRepository ootdImageRepository;
    private final MemberService memberService;
    private final CollageImageService collageImageService;
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final MetadataExtractor metadataExtractor;
    private final AIService aiService;
    private final WeatherDataService weatherDataService;

    @Transactional
    public OotdImageResponseDTO.uploadOOTD segmentImage(Long memberId, MultipartFile ootdImage, Set<Category> categorySet) throws Exception {
        Member member = getMemberById(memberId);
        validateImage(ootdImage);

        MetadataDTO metadata = extractMetadata(ootdImage);
        String ootdImageUrl = uploadImageToS3(ootdImage, member);

        String aiUrlsResponse = aiService.callSegmentApi(ootdImage, categorySet);
        log.info("Segment API Response: {}", aiUrlsResponse);

        String weatherApiResponse = aiService.callAddWeatherApi(metadata.getDate(), metadata.getLatitude(), metadata.getLongitude());
        log.info("Add Weather API Response: {}", weatherApiResponse);

        List<String> collageImagesUrl = aiService.parseApiResponse(aiUrlsResponse);
        Long weatherDataId = aiService.parseWeatherResponse(weatherApiResponse);

        // 트랜잭션 분리 후 조회
        WeatherData weatherData = weatherDataService.fetchWeatherDataById(weatherDataId);
        saveOotdImage(member, weatherData, ootdImageUrl, metadata);

        for (String url : collageImagesUrl) {
            collageImageService.saveCollageImage(member, url, weatherData);
        }
        return new OotdImageResponseDTO.uploadOOTD(ootdImageUrl, collageImagesUrl);
    }

    private Member getMemberById(Long memberId) {
        log.info("Fetching member with ID: {}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private void validateImage(MultipartFile ootdImage) {
        if (ootdImage.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
        }
    }

    private MetadataDTO extractMetadata(MultipartFile ootdImage) throws Exception {
        log.info("Extracting metadata from image...");
        MetadataDTO metadata = metadataExtractor.extract(ootdImage);
        if (metadata == null) {
            throw new RuntimeException("메타데이터 추출 실패");
        }
        return metadata;
    }

    private String uploadImageToS3(MultipartFile ootdImage, Member member) throws IOException {
        log.info("Uploading image to S3...");
        return amazonS3Manager.uploadFile("ootd/", ootdImage, member.getKakaoId());
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
