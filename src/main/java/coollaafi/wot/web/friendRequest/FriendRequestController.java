package coollaafi.wot.web.friendRequest;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/request")
    @Operation(summary = "친구 요청 API", description = "친구 요청에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<FriendRequestDTO.responseDTO> sendFriendRequest(@RequestParam("senderId") Long senderId,
                                                                       @RequestParam("receiverId") Long receiverId) {
        FriendRequestDTO.responseDTO friendRequest = friendRequestService.sendFriendRequest(senderId, receiverId);
        return ApiResponse.onSuccess(friendRequest);
    }

}
