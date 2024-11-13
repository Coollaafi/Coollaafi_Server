package coollaafi.wot.web.oauth2;

import coollaafi.wot.web.auth.AuthDTO;
import coollaafi.wot.web.auth.AuthService;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthService authService;

    public CustomOAuth2UserService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오 사용자 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Long kakaoId = (Long) attributes.get("id");

        // AuthService의 kakaoLogin 호출하여 사용자 등록 및 JWT 생성
        AuthDTO.LoginResponse loginResponse = authService.kakaoLogin(kakaoId);
        return new CustomOAuth2User(oAuth2User, loginResponse.getAccessToken(), loginResponse.getRefreshToken(),
                loginResponse.isNewMember());
    }
}
