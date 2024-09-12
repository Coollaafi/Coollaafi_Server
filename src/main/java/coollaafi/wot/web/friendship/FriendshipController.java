package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<FriendshipDTO.responseDTO> acceptFriendRequest(@RequestParam Long id){
        FriendshipDTO.responseDTO friend = friendshipService.acceptFriendRequest(id);
        return ApiResponse.onSuccess(friend);
    }

    @PostMapping("/request/reject")
    @Operation(summary = "친구 요청 거절 API", description = "친구 요청을 거절할 때 필요한 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<Void> rejectFriendRequest(@RequestParam Long id){
        friendshipService.rejectFriendRequest(id);
        return ApiResponse.onSuccess(null);
    }
}
