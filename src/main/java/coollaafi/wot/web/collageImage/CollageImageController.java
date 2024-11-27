package coollaafi.wot.web.collageImage;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.ai.AIService;
import coollaafi.wot.web.collageImage.CollageImageDTO.RecommendationRequireDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CollageImageController {
    private final AIService aiService;
    private final CollageImageService collageImageService;

    @GetMapping("/recommend-outfit")
    @Operation(summary = "Ai'옷추천 API", description = "현재 사용자 위치의 위도 경도를 입력해주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<String>> createLookBookPost(
            @RequestParam("memberId") Long memberId,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        String urls = aiService.getRecommendedOutfit(
                memberId, latitude, longitude);
        log.info("Calling FastAPI with URL: {}", urls);
        List<String> dates = aiService.parseRecommendedOutfit(urls);
        log.info("Calling FastAPI with dates: {}", dates);
        List<String> clothesUrls = collageImageService.getCollageImageByDate(memberId, dates);

        return ApiResponse.onSuccess(clothesUrls);
    }
}
