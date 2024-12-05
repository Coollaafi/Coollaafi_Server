package coollaafi.wot.web.reply;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    // 대댓글 작성 API
    @PostMapping("/")
    @Operation(summary = "대댓글 작성 API", description = "대댓글을 작성할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<ReplyResponseDTO.ReplyCreateDTO> createReply(
            @RequestParam("commentId") Long commentId,
            @RequestParam("memberId") Long memberId,
            @RequestParam("content") String content) {
        ReplyResponseDTO.ReplyCreateDTO reply = replyService.createReply(commentId, memberId, content);
        return ApiResponse.onSuccess(reply);
    }

    // 댓글에 달린 대댓글 조회 API
    @GetMapping("/{commentId}")
    @Operation(summary = "대댓글 조회 API", description = "대댓글을 조회 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<ReplyResponseDTO.ReplyGetDTO>> getRepliesByCommentId(
            @RequestParam("commentId") Long commentId) {
        List<ReplyResponseDTO.ReplyGetDTO> replies = replyService.getRepliesByCommentId(commentId);
        return ApiResponse.onSuccess(replies);
    }
}
