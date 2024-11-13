package coollaafi.wot.web.oauth2;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    @Getter
    private final String accessToken;
    @Getter
    private final String refreshToken;
    private final boolean isNewMember;

    public CustomOAuth2User(OAuth2User oAuth2User, String accessToken, String refreshToken, boolean isNewMember) {
        this.oAuth2User = oAuth2User;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isNewMember = isNewMember;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        // 'name' 키가 없을 경우 'nickname'이나 'id' 필드를 사용
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return (String) attributes.getOrDefault("name",
                attributes.getOrDefault("nickname", String.valueOf(attributes.get("id"))));
    }

    public boolean isNewMember() {
        return isNewMember;
    }
}
