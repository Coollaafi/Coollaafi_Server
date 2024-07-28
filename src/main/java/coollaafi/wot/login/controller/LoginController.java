package coollaafi.wot.login.controller;

import coollaafi.wot.kakao.KakaoService;
import coollaafi.wot.login.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class LoginController {
    private final KakaoService kakaoService;

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Autowired
    public LoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    };


    @GetMapping("/login")
    public String login() {
        return "Hello World";
    }


    @GetMapping("/login/oauth2/code/kakao")
    public LoginResponseDTO kakao(@RequestParam("code") String code) {
        return kakaoService.kakaoLogin(code, redirectUri);
    }

    @GetMapping("/kakao")
    public RedirectView kakaoLogin() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoClientId + "&redirect_uri=" + redirectUri;
        return new RedirectView(kakaoLoginUrl);
    }
}
