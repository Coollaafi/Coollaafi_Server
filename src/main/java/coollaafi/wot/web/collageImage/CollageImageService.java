package coollaafi.wot.web.collageImage;

import coollaafi.wot.web.member.entity.Member;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Transactional
    public List<String> recommendOutfit(Long memberId, Double latitude, Double longitude) {
        return new ArrayList<>(Arrays.asList(
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7379993212_shoes.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7868912374_bottom.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes4767268203_top.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7379993212_shoes.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7868912374_bottom.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes4767268203_top.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7379993212_shoes.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes7868912374_bottom.jpg",
                "https://wot-bucket.s3.ap-northeast-2.amazonaws.com/segmented_img/jacket%2Cblouse%2Cskirt%2Cshoes4767268203_top.jpg"));  // 사진과 메타데이터 저장
    }
}
