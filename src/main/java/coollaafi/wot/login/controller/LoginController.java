package coollaafi.wot.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import coollaafi.wot.kakao.KakaoService;
import coollaafi.wot.login.dto.LoginResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class LoginController {
    private final KakaoService kakaoService;


    @Autowired
    public LoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    };

    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("http://localhost:8080/logout")
    private String logout_redirectUri;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @GetMapping("/login/oauth2/code/kakao")
    public LoginResponseDTO kakao(@RequestParam("code") String code, HttpSession session) {
        LoginResponseDTO member = kakaoService.kakaoLogin(code, redirectUri);
        if (member != null){
            session.setAttribute("loginMember", member);
            session.setMaxInactiveInterval(60 * 30);
            session.setAttribute("kakaoToken", member.getAccessToken());
        }
        return member;
    }

    @GetMapping("/kakao")
    public RedirectView kakaoLogin() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoClientId + "&redirect_uri=" + redirectUri;
        return new RedirectView(kakaoLoginUrl);
    }

    //카카오 일반 로그아웃(by accessToken)
    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");
        System.out.println(accessToken);

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e){
                throw new RuntimeException(e);
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }else {
            System.out.println("accessToken is null");
        }

        return "redirect:/";
    }

    //카카오 연결 끊기(by accessToken)
    @GetMapping("/kakao/delete")
    public String kakaoUnlink(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");
        System.out.println(accessToken);

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoService.KaKaoUnlink(accessToken);
            } catch (JsonProcessingException e){
                throw new RuntimeException(e);
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }else {
            System.out.println("accessToken is null");
        }

        return "redirect:/";
    }

    @GetMapping("/kakao/logout/withAccount")
    public RedirectView kakaoLogoutWithAccount() {
        String uri = "https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId + "&logout_redirect_uri=" + logout_redirectUri;
        return new RedirectView(uri);

    }
}
