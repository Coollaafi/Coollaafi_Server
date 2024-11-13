package coollaafi.wot.web.auth;

import coollaafi.wot.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    @Operation(summary = "사용자 정보 조회 API", description = "인증된 사용자의 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<AuthDTO.UserInfo> getUserInfo(@RequestParam("accessToken") String accessToken) {
        AuthDTO.UserInfo userInfo = authService.getUserInfo(accessToken);
        return ApiResponse.onSuccess(userInfo);
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refreshAccessToken(@RequestParam("refreshToken") String refreshToken) {
        // Refresh Token을 통해 새로운 Access Token 발급
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        if (newAccessToken == null) {
            return ApiResponse.onFailure("401", "Invalid or expired refresh token", null);
        }
        return ApiResponse.onSuccess(newAccessToken);
    }
}