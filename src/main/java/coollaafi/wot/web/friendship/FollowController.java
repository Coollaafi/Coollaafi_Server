package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.member.MemberDTO.MemberBasedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/request")
    @Operation(summary = "팔로우 API", description = "팔로우 시 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<FollowDTO.responseDTO> followRequest(
            @RequestParam("followerId") Long followerId,
            @RequestParam("followeeId") Long followeeId) {
        FollowDTO.responseDTO friend = followService.followRequest(followerId, followeeId);
        return ApiResponse.onSuccess(friend);
    }

    @PostMapping("/reject")
    @Operation(summary = "팔로우 취소 API", description = "팔로우 취소에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Null> RejectFollowRequest(
            @RequestParam("followerId") Long followerId,
            @RequestParam("followeeId") Long followeeId) {
        followService.rejectFollowRequest(followerId, followeeId);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/{memberId}/followers")
    @Operation(summary = "팔로워 목록 조회", description = "특정 회원의 팔로워 목록을 조회합니다. 최대 20명까지 반환.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<MemberBasedDTO>> getFollowers(@PathVariable("memberId") Long memberId) {
        List<MemberBasedDTO> followers = followService.getFollowers(memberId);
        return ApiResponse.onSuccess(followers);
    }

    @GetMapping("/{memberId}/followees")
    @Operation(summary = "팔로우 목록 조회", description = "특정 회원이 팔로우하는 사람들의 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<MemberBasedDTO>> getFollowees(@PathVariable("memberId") Long memberId) {
        List<MemberBasedDTO> followees = followService.getFollowees(memberId);
        return ApiResponse.onSuccess(followees);
    }
}
