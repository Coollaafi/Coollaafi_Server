package coollaafi.wot.web.photo;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드 API", description = "이미지 업로드시 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Void> uploadPhotos(@RequestParam("photos") List<MultipartFile> photos,
                                            @RequestParam("memberId") Long memberId) throws IOException {
        photoService.uploadPhotos(photos, memberId);
        return ApiResponse.onSuccess(null);
    }
}
