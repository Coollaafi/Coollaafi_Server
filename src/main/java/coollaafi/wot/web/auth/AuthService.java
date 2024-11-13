package coollaafi.wot.web.auth;

import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.web.auth.AuthDTO.UserInfo;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    public AuthDTO.LoginResponse kakaoLogin(Long kakaoId) {
        // DB에서 사용자 조회 또는 신규 사용자 등록
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> registerNewMember(kakaoId));

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        member.setAccessToken(accessToken);
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        return new AuthDTO.LoginResponse(accessToken, refreshToken, member.getId());
    }

    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("code", code);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        return (String) responseBody.get("access_token");
    }

    private Long getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);

        return ((Long) response.getBody().get("id"));
    }

    private Member registerNewMember(Long kakaoId) {
        Member newMember = new Member();
        newMember.setKakaoId(kakaoId);
        return memberRepository.save(newMember);
    }

    public UserInfo getUserInfo(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(request);
        Optional<Member> member = memberRepository.findById(memberId);
        return member.map(m -> new AuthDTO.UserInfo(m.getAccessToken(), m.getRefreshToken(), m.getId(), m.getKakaoId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
