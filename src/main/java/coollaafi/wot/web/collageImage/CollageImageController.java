package coollaafi.wot.web.collageImage;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CollageImageController {
    private final CollageImageService collageImageService;

    @GetMapping("/recommend-outfit")
    @Operation(summary = "Ai'옷추천 API", description = "현재 사용자 위치의 위도 경도를 입력해주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<String>> getAllPost(
            @RequestParam("memberId") Long memberId,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        List<String> result = collageImageService.recommendOutfit(memberId, latitude, longitude);
        return ApiResponse.onSuccess(result);
    }
}
