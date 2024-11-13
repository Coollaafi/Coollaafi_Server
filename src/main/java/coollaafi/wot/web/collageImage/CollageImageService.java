package coollaafi.wot.web.collageImage;

import coollaafi.wot.web.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollageImageService {
    private final CollageImageRepository collageImageRepository;

    @Transactional
    public CollageImage saveCollageImage(Member member, String s3url, WeatherData weatherData) {
        // DB에 사진 정보와 메타데이터 저장
        CollageImage collageImage = CollageImage.builder()
                .member(member)
                .s3url(s3url)
                .weatherData(weatherData)
                .build();

        return collageImageRepository.save(collageImage);  // 사진과 메타데이터 저장
    }
}
