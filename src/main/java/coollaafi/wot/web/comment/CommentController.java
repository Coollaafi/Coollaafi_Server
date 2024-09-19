package coollaafi.wot.web.comment;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/")
    @Operation(summary = "댓글 작성 API", description = "댓글을 작성할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<CommentResponseDTO.CommentCreateDTO> createComment(@RequestParam Long postId, @RequestParam Long memberId, @RequestParam String content) {
        CommentResponseDTO.CommentCreateDTO comment = commentService.createComment(postId, memberId, content);
        return ApiResponse.onSuccess(comment);
    }

    // 게시글에 달린 댓글 조회 API
    @GetMapping("/{postId}")
    @Operation(summary = "댓글 조회 API", description = "댓글을 조회 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<CommentResponseDTO.CommentWithReplyDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDTO.CommentWithReplyDTO> comments = commentService.getCommentsByPostId(postId);
        return ApiResponse.onSuccess(comments);
    }
}
