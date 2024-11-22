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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ai-clothes-recommendation")
@RequiredArgsConstructor
public class CollageImageController {
    private final AIService aiService;
    private final CollageImageService collageImageService;

    @PostMapping(value = "/")
    @Operation(summary = "AI's 옷추천 API", description = "오늘의 옷 추천 결과를 리턴합니다")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<String>> createLookBookPost(
        @RequestBody RecommendationRequireDTO requireDTO) {
        String urls = aiService.getRecommendedOutfit(
                requireDTO.getMemberId(),
                requireDTO.getLatitude(),
                requireDTO.getLongitude());
        log.info("Calling FastAPI with URL: {}", urls);
        List<String> dates = aiService.parseRecommendedOutfit(urls);
        log.info("Calling FastAPI with dates: {}", dates);
        List<String> clothesUrls = collageImageService.getCollageImageByDate(requireDTO.getMemberId(), dates);

        return ApiResponse.onSuccess(clothesUrls);
    }
}
