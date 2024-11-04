package coollaafi.wot.web.auth;

import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.web.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider,
                          MemberRepository memberRepository) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

//    @PostMapping("/kakao/login")
//    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 후 리다이렉트된 URL을 통해 인증 코드를 받아 로그인 처리하는 API입니다.")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
//    })
//    public ApiResponse<LoginResponse> kakaoLogin(@RequestParam("code") String code) {
//        AuthDTO.LoginResponse loginResponse = authService.kakaoLogin(code);
//        return ApiResponse.onSuccess(loginResponse);
//    }

    @GetMapping("/me")
    @Operation(summary = "사용자 정보 조회 API", description = "인증된 사용자의 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    public ApiResponse<AuthDTO.UserInfo> getUserInfo(HttpServletRequest request) {
        AuthDTO.UserInfo userInfo = authService.getUserInfo(request);
        return ApiResponse.onSuccess(userInfo);
    }

//    @PostMapping("/refresh")
//    public ApiResponse<AuthDTO.LoginResponse> refreshAccessToken(@RequestParam String refreshToken) {
//        // Refresh token 검증
//        if (!jwtTokenProvider.validateToken(refreshToken)) {
//            return null; // 유효하지 않은 경우 401 반환
//        }
//
//        // refresh token에서 사용자 ID 추출
//        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
//
//        // DB에서 사용자 조회
//        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // refresh token과 일치하는지 확인 (DB에 저장된 토큰과 비교)
//        if (!refreshToken.equals(member.getRefreshToken())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 불일치 시 401 반환
//        }
//
//        // 새 access token 생성
//        String newAccessToken = jwtTokenProvider.createToken(userId);
//
//        // 새로운 토큰 반환
//        return ApiResponse.onSuccess(new AuthDTO.LoginResponse(newAccessToken, refreshToken));
//    }
}