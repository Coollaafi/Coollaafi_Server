package coollaafi.wot.web.ootdImage;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.post.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ootd")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://coollaafi.shop", "https://coollaafi-frontend.vercel.app",})
public class OotdImageController {
    private final OotdImageService ootdImageService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "collage 이미지 생성", description = "ootd이미지와 카테고리를 입력하면 콜라주 이미지를 생성해 리턴합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<OotdImageResponseDTO.uploadOOTD> createLookBookPost(
            @RequestParam("memberId") Long memberId,
            @RequestParam("ootdImage") MultipartFile ootdImage,
            @RequestParam("categorySet") Set<Category> categorySet) {
        try {
            OotdImageResponseDTO.uploadOOTD segmentImages = ootdImageService.segmentImage(memberId, ootdImage,
                    categorySet);
            return ApiResponse.onSuccess(segmentImages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
