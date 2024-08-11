package coollaafi.wot.web.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AIService {
    private final RestTemplate restTemplate;
    public URL generateLookBookImage(URL ootdImage){
        String aiServiceUrl = "http://python-ai-service-url/generate";  // Python 서버 URL
        try {
            // 요청 본문 생성
            String requestJson = "{\"ootdImage\":\"" + ootdImage.toString() + "\"}";

            // Python 서버에 요청 보내기
            String responseJson = restTemplate.postForObject(aiServiceUrl, requestJson, String.class);

            // 응답에서 lookbook 이미지 URL 추출 (예시로 단순 파싱)
            // 실제 응답 구조에 따라 파싱 로직 수정 필요
            String lookbookImageUrl = parseLookbookImageUrlFromResponse(responseJson);

            return new URL(lookbookImageUrl);
        } catch (RestClientException | MalformedURLException e) {
            // 예외 처리
            e.printStackTrace();
            // 실패 시 기본 URL 반환 또는 예외 재발생
            try {
                return new URL("http://example.com/default_lookbook_image.jpg");
            } catch (MalformedURLException ex) {
                throw new RuntimeException("Failed to generate lookbook image and default URL is malformed", ex);
            }
        }
    }
    private String parseLookbookImageUrlFromResponse(String responseJson) {
        // 단순 예시 파싱 로직
        // 실제 응답 구조에 따라 JSON 파싱 라이브러리 사용 권장
        return responseJson.replace("{\"lookbookImage\":\"", "").replace("\"}", "");
    }
}
