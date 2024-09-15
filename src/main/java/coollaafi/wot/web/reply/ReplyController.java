package coollaafi.wot.web.reply;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    // 대댓글 작성 API
    @PostMapping("/")
    @Operation(summary = "대댓글 작성 API", description = "대댓글을 작성할 때 필요한 API입니다.")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<ReplyResponseDTO.ReplyCreateDTO> createReply(@RequestParam Long commentId, @RequestParam Long memberId, @RequestParam String content) {
        ReplyResponseDTO.ReplyCreateDTO reply = replyService.createReply(commentId, memberId, content);
        return ApiResponse.onSuccess(reply);
    }

    // 댓글에 달린 대댓글 조회 API
    @GetMapping("/{commentId}")
    @Operation(summary = "대댓글 조회 API", description = "대댓글을 조회 때 필요한 API입니다.")
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<ReplyResponseDTO.ReplyGetDTO>> getRepliesByCommentId(@RequestParam Long commentId) {
        List<ReplyResponseDTO.ReplyGetDTO> replies = replyService.getRepliesByCommentId(commentId);
        return ApiResponse.onSuccess(replies);
    }
}
