package coollaafi.wot.web.postPrefer;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/prefer")
@RequiredArgsConstructor
public class PostPreferController {
    private final PostPreferService postPreferService;

    @PostMapping
    @Operation(summary = "게시글 좋아요 등록 API", description = "게시글을 좋아요 할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<PostPreferResponseDTO> createPostPrefer(@RequestBody PostPreferRequestDTO request) {
        PostPreferResponseDTO postPrefer = postPreferService.saveSpacePrefer(request);
        return ApiResponse.onSuccess(postPrefer);
    }

    @DeleteMapping("/{postPreferId}")
    @Operation(summary = "게시글 좋아요 삭제 API", description = "게시글 좋아요를 해제할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Void> deletePostPrefer(@PathVariable("postPreferId") Long postPreferId) {
        postPreferService.deletePostPrefer(postPreferId);
        return ApiResponse.onSuccess(null);
    }
}
