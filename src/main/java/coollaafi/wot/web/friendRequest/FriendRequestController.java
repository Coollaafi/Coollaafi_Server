package coollaafi.wot.web.friendRequest;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/request/reject")
    @Operation(summary = "친구 요청 거절, 취소 API", description = "친구 요청을 거절 및 취소할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Void> rejectFriendRequest(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId) {
        friendRequestService.preFriendRequest(senderId, receiverId);
        return ApiResponse.onSuccess(null);
    }


    @GetMapping("/send")
    @Operation(summary = "친구 요청 목록 조회 API", description = "친구 요청에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<FriendRequestDTO.responseGetDTO>> getSendFriendRequest(
            @RequestParam("memberId") Long memberId) {
        List<FriendRequestDTO.responseGetDTO> friendRequest = friendRequestService.getSendFriendRequest(memberId);
        return ApiResponse.onSuccess(friendRequest);
    }

    @GetMapping("/receive")
    @Operation(summary = "친구 요청 목록 조회 API", description = "친구 요청에 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<List<FriendRequestDTO.responseGetDTO>> getReceiveFriendRequest(
            @RequestParam("memberId") Long memberId) {
        List<FriendRequestDTO.responseGetDTO> friendRequest = friendRequestService.getReceiveFriendRequest(memberId);
        return ApiResponse.onSuccess(friendRequest);
    }

}
