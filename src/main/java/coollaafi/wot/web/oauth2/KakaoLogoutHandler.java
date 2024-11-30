package coollaafi.wot.web.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoLogoutHandler implements LogoutHandler {

    private final RestTemplate restTemplate;

    public KakaoLogoutHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 세션에서 Kakao Access Token 가져오기
        String accessToken = (String) request.getSession().getAttribute("accessToken");

        if (accessToken != null) {
            try {
                // Kakao 로그아웃 API 호출
                restTemplate.postForEntity(
                        "https://kapi.kakao.com/v1/user/logout",
                        null,
                        String.class,
                        Map.of("Authorization", "Bearer " + accessToken)
                );
                // 세션에서 Access Token 삭제
                request.getSession().removeAttribute("accessToken");
            } catch (Exception e) {
                e.printStackTrace(); // 로그 에러 처리
            }
        }
    }
}
