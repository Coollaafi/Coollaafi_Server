package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @PostMapping("/request/accept")
    @Operation(summary = "친구 요청 수락 API", description = "친구 요청을 수락할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<FriendshipDTO.responseDTO> acceptFriendRequest(
            @RequestParam("friendRequestId") Long friendRequestId) {
        FriendshipDTO.responseDTO friend = friendshipService.acceptFriendRequest(friendRequestId);
        return ApiResponse.onSuccess(friend);
    }

    @PostMapping("/request/reject/{friendRequestId}")
    @Operation(summary = "친구 요청 거절 API", description = "친구 요청을 거절할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Void> rejectFriendRequest(@Parameter @PathVariable("friendRequestId") Long friendRequestId) {
        friendshipService.rejectFriendRequest(friendRequestId);
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteFriend(@RequestParam("memberId1") Long memberId1,
                                          @RequestParam("memberId2") Long memberId2) {
        friendshipService.deleteFriend(memberId1, memberId2);
        return ApiResponse.onSuccess(null);
    }
}
