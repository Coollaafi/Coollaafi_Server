package coollaafi.wot.web.auth;

import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.web.auth.AuthDTO.UserInfo;
import coollaafi.wot.web.member.entity.Member;
import coollaafi.wot.web.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        boolean isNewMember = false;

        Member member = memberRepository.findByKakaoId(kakaoId);
        if (member == null) {
            member = registerNewMember(kakaoId);
            isNewMember = true;
        }

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        member.setAccessToken(accessToken);
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        return new AuthDTO.LoginResponse(accessToken, refreshToken, member.getId(), isNewMember);
    }

    private Member registerNewMember(Long kakaoId) {
        Member newMember = new Member();
        newMember.setKakaoId(kakaoId);
        return memberRepository.save(newMember);
    }

    public UserInfo getUserInfo(String token) {
        Long memberId = jwtTokenProvider.getUserIdFromToken(token);
        Optional<Member> member = memberRepository.findById(memberId);
        return member.map(m -> new AuthDTO.UserInfo(m.getAccessToken(), m.getRefreshToken(), m.getId(), m.getKakaoId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String refreshAccessToken(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return null; // 유효하지 않은 경우 null 반환
        }

        // 2. Refresh Token에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 3. 사용자 조회
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null || !member.getRefreshToken().equals(refreshToken)) {
            return null; // 사용자나 토큰 불일치 시 null 반환
        }

        // 4. 새로운 Access Token 생성
        String newAceessToken = jwtTokenProvider.createToken(member.getId());
        member.setAccessToken(newAceessToken);
        memberRepository.save(member);

        return newAceessToken;
    }

}
