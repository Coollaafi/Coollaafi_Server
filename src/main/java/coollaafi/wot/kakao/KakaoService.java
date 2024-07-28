package coollaafi.wot.kakao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import coollaafi.wot.jwt.AuthTokens;
import coollaafi.wot.jwt.AuthTokensGenerator;
import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.login.dto.LoginResponseDTO;
import coollaafi.wot.member.repository.MemberRepository;
import coollaafi.wot.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class KakaoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    public LoginResponseDTO kakaoLogin(String code, String redirectUri) {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code, redirectUri);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

        // 3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponseDTO kakaoUserResponse = kakaoUserLogin(userInfo);

        return kakaoUserResponse;
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code, String redirectUri) {
        logger.info("Requesting access token with code: {} and redirectUri: {}", code, redirectUri);

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            logger.info("Received response: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while requesting access token: " + e.getMessage());
            throw new RuntimeException(e);
        }

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출
    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response: " + e.getMessage());
            throw new RuntimeException(e);
        }

        Long id = jsonNode.get("id").asLong();
        System.out.println(id);
        userInfo.put("id", id);

        return userInfo;
    }

    // 3. 카카오ID로 회원가입 & 로그인 처리
    private LoginResponseDTO kakaoUserLogin(HashMap<String, Object> userInfo) {
        Long uid = Long.valueOf(userInfo.get("id").toString());
        Member kakaoUser = memberRepository.findById(uid).orElse(null);

        if (kakaoUser == null) {
            kakaoUser = new Member();
            kakaoUser.setId(uid);
            memberRepository.save(kakaoUser);
        }

        AuthTokens token = authTokensGenerator.generate(uid.toString());
        return new LoginResponseDTO(uid, token);
    }
}
