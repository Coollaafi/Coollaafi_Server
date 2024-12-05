package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendRequest.FriendRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final FriendRequestService friendRequestService;

    @PostMapping("/request/accept")
    @Operation(summary = "친구 요청 수락 API", description = "친구 요청을 수락할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<FriendshipDTO.responseDTO> acceptFriendRequest(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId) {
        FriendRequest request = friendRequestService.preFriendRequest(senderId, receiverId);
        FriendshipDTO.responseDTO friend = friendshipService.acceptFriendRequest(request);
        return ApiResponse.onSuccess(friend);
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteFriend(@RequestParam("memberId1") Long memberId1,
                                          @RequestParam("memberId2") Long memberId2) {
        friendshipService.deleteFriend(memberId1, memberId2);
        return ApiResponse.onSuccess(null);
    }
}
