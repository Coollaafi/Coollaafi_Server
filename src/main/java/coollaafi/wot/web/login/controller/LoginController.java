package coollaafi.wot.web.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coollaafi.wot.apiPayload.ApiResponse;
import coollaafi.wot.web.kakao.KakaoService;
import coollaafi.wot.web.login.dto.KakaoUnlinkDTO;
import coollaafi.wot.web.login.dto.LoginResponseDTO;
import coollaafi.wot.web.login.dto.MemberResponseDTO;
import coollaafi.wot.web.member.service.MemberService;
import coollaafi.wot.web.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    @Autowired
    public LoginController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("http://localhost:8080")
    private String logout_redirectUri;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @GetMapping("/member_info/{uid}")
    public ApiResponse<MemberResponseDTO> getMemberInfo(@PathVariable(name = "uid") Long uid){
        Member member = memberService.getMemberByUid(uid);
        return ApiResponse.onSuccess(MemberResponseDTO.builder()
                .memberId(member.getId())
                .build());
    }

    @GetMapping("/login/oauth2/code/kakao")
    public LoginResponseDTO kakao(@RequestParam("code") String code) {
        return kakaoService.kakaoLogin(code, redirectUri);
    }

    @GetMapping("/kakao")
    public String kakaoLogin() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoClientId + "&redirect_uri=" + redirectUri + "&response_type=code";
    }

    //카카오 일반 로그아웃(by accessToken)
    @PostMapping("/kakao/logout")
    public String kakaoLogout(@RequestBody Long uid) {
        try {
            kakaoService.kakaoDisconnect(kakaoService.getAccessToken(uid));
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    //카카오 연결 끊기(by accessToken)
    @PostMapping("/kakao/delete")
    public String kakaoUnlink(@RequestBody KakaoUnlinkDTO request) {
        try {
            kakaoService.KaKaoUnlink(kakaoService.getAccessToken(request.getUid()));
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    @GetMapping("/kakao/logout/withAccount")
    public String kakaoLogoutWithAccount() {
        return "https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId + "&logout_redirect_uri=" + logout_redirectUri;

    }
}
