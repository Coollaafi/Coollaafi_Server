package coollaafi.wot.web.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final String FRONTEND_URL = "http://localhost:3000"; // 프론트엔드 URL

    // RedirectStrategy 객체를 직접 생성
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();

        // JSON 응답 생성
        String accessToken = customUser.getAccessToken();
        String refreshToken = customUser.getRefreshToken();
        boolean isNewMember = customUser.isNewMember();

        // 프론트엔드 URL로 리다이렉트할 때 임시로 필요한 정보 전달
        String frontendRedirectUrl = String.format(
                "%s/login/success?accessToken=%s&refreshToken=%s&isNewMember=%b",
                FRONTEND_URL, accessToken, refreshToken, isNewMember);

        // 프론트엔드로 리다이렉트
        redirectStrategy.sendRedirect(request, response, frontendRedirectUrl);
    }
}
