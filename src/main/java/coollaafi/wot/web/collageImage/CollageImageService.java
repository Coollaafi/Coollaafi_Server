package coollaafi.wot.web.collageImage;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.handler.MemberHandler;
import coollaafi.wot.web.member.repository.MemberRepository;
import coollaafi.wot.web.weatherData.WeatherData;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollageImageService {
    private final CollageImageRepository collageImageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveCollageImage(Member member, String s3url, WeatherData weatherData) {
        // DB에 사진 정보와 메타데이터 저장
        CollageImage collageImage = CollageImage.builder()
                .member(member)
                .s3url(s3url)
                .weatherData(weatherData)
                .build();

        collageImageRepository.save(collageImage);  // 사진과 메타데이터 저장
    }

    @Transactional
    public List<String> getCollageImageByDate(Long memberId, List<String> dates) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Date> convertedDates = convertStringsToDates(dates);

        List<CollageImage> collageImages = new ArrayList<>();
        for (Date date : convertedDates) {
            collageImages.addAll(
                    collageImageRepository.findCollageImageByMemberAndWeatherData_Date(member, date)
            );
        }

        return collageImages.stream()
                .map(CollageImage::getS3url) // Assuming CollageImage has a getUrl() method
                .collect(Collectors.toList());
    }

    private List<Date> convertStringsToDates(List<String> dateStrings) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ensure this matches your date format
        List<Date> dateList = new ArrayList<>();
        for (String dateString : dateStrings) {
            try {
                dateList.add(dateFormat.parse(dateString)); // Convert String to Date
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format: " + dateString, e);
            }
        }
        return dateList;
    }
}
