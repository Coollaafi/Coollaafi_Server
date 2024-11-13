package coollaafi.wot.web.post;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(name = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 API", description = "게시글을 올릴 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.PostCreateDTO> createPost(
            @RequestPart("requestDTO") PostRequestDTO.PostCreateRequestDTO requestDTO,
            @RequestPart("lookbookImage") MultipartFile lookbookImage) throws IOException, InterruptedException {
        PostResponseDTO.PostCreateDTO responseDTO = postService.createPost(requestDTO, lookbookImage);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/")
    @Operation(summary = "게시글 전체 조회 API", description = "게시글 전체 조회에 필요한 API입니다. 현재 사용자의 친구가 일주일 내에 올린 게시글이 조회됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<PostResponseDTO.AllPostGetDTO>> getAllPost(@Valid @RequestParam("memberId") Long memberId) {
        List<PostResponseDTO.AllPostGetDTO> responseDTO = postService.getAllPost(memberId);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회 API", description = "게시글 조회에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostResponseDTO.OnePostGetDTO> getPost(@Valid @RequestParam("memberId") Long memberId,
                                                              @Valid @PathVariable("postId") Long postId) {
        PostResponseDTO.OnePostGetDTO responseDTO = postService.getPost(postId, memberId);
        return ApiResponse.onSuccess(responseDTO);
    }

    @GetMapping("/calendar/{memberId}")
    @Operation(summary = "홈화면 캘린더 조회 API", description = "캘린더 조회에 필요한 API입니다. year, month를 넣어주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<CalendarDTO> getPost(@Valid @PathVariable("memberId") Long memberId,
                                            @RequestParam("year") Integer year,
                                            @RequestParam("month") Integer month) {
        CalendarDTO responseDTO = postService.getCalendar(memberId, year, month);
        return ApiResponse.onSuccess(responseDTO);
    }
}
