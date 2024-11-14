package coollaafi.wot.web.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();

        // JSON 응답 생성
        String accessToken = customUser.getAccessToken();
        String refreshToken = customUser.getRefreshToken();
        boolean isNewMember = customUser.isNewMember();
        String kakaoId = customUser.getName();

        // JSON 형태로 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"accessToken\":\"%s\", \"refreshToken\":\"%s\", \"isNewMember\":\"%b\", \"userId\":%s}", accessToken,
                refreshToken,
                isNewMember, kakaoId));
    }
}
