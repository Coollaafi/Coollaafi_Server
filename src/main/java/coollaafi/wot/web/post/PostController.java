package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/lookbook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 API - lookbook 이미지 생성", description = "게시글을 작성시 룩북 이미지를 생성할 때 필요한 API입니다. 입력 받은 ootd 이미지를 ai로 넘겨 lookbook 이미지를 생성해 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.LookbookDTO> createLookBookPost(@Valid @RequestParam Long memberId, @RequestParam("ootdImage") MultipartFile ootdImage) throws IOException {
        if (ootdImage.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
        }

        PostResponseDTO.LookbookDTO responseDTO = postService.createLookbook(memberId, ootdImage);
        return ApiResponse.onSuccess(responseDTO);
    }

    @PostMapping(value = "/first")
    @Operation(summary = "게시글 작성 API - 중간저장", description = "게시글을 작성시 룩북 생성 이후 다음으로 넘어갈 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.PostCreateDTO> createLookBookPost(@Valid @RequestParam Long memberId, String ootd, String lookbook) {
        PostResponseDTO.PostCreateDTO responseDTO = postService.firstCreatePost(memberId, ootd, lookbook);
        return ApiResponse.onSuccess(responseDTO);
    }

    @PostMapping("/second")
    @Operation(summary = "게시글 작성 API - 최종 저장", description = "게시글 두번째 단계에서 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.PostCreateDTO> createPost(@Valid @RequestBody PostRequestDTO.PostCreateRequestDTO requestDTO) {
        PostResponseDTO.PostCreateDTO responseDTO = postService.createPost(requestDTO);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/")
    @Operation(summary = "게시글 전체 조회 API", description = "게시글 전체 조회에 필요한 API입니다. 현재 사용자의 친구가 일주일 내에 올린 게시글이 조회됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDTO.AllPostGetDTO>> getAllPost(@Valid @RequestParam Long memberId) {
        List<PostResponseDTO.AllPostGetDTO> responseDTO = postService.getAllPost(memberId);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회 API", description = "게시글 조회에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.OnePostGetDTO> getPost(@Valid @RequestParam Long memberId, @Valid @PathVariable Long postId) {
        PostResponseDTO.OnePostGetDTO responseDTO = postService.getPost(postId, memberId);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/calendar/{memberId}")
    @Operation(summary = "홈화면 캘린더 조회 API", description = "캘린더 조회에 필요한 API입니다. year, month를 넣어주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<CalendarDTO> getPost(@Valid @PathVariable Long memberId, @RequestParam Integer year, @RequestParam Integer month) {
        CalendarDTO responseDTO = postService.getCalendar(memberId, year, month);
        return ApiResponse.onSuccess(responseDTO);
    }
}
