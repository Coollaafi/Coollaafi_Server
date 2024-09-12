package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/lookbook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 API - lookbook 이미지 생성", description = "게시글을 처음 작성할 때 필요한 API입니다. 입력 받은 ootd 이미지를 ai로 넘겨 lookbook 이미지를 생성해 저장합니다.")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.PostCreateLookBookResultDTO> createLookBookPost(@Valid @RequestParam Long memberId, @RequestParam("ootdImage") MultipartFile ootdImage) {
        PostResponseDTO.PostCreateLookBookResultDTO responseDTO = postService.createPostLookbook(memberId, ootdImage);
        return ApiResponse.onSuccess(responseDTO);
    }

    @PostMapping("/")
    @Operation(summary = "게시글 작성 API", description = "게시글 두번째 단계에서 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.PostCreateResultDTO> createPost(@Valid @RequestBody PostRequestDTO.PostCreateRequestDTO requestDTO) {
        PostResponseDTO.PostCreateResultDTO responseDTO = postService.createPost(requestDTO);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "게시글 조회 API", description = "게시글 조회에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDTO.PostGetResultDTO>> getPost(@Valid @PathVariable Long memberId) {
        List<PostResponseDTO.PostGetResultDTO> responseDTO = postService.getPost(memberId);
        return ApiResponse.onSuccess(responseDTO);
    }
}
