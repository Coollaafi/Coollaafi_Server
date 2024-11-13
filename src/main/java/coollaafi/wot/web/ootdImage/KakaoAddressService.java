package coollaafi.wot.web.ootdImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoAddressService {

    //    @Value("${kakao.api.key}") // application.properties에 저장된 카카오 API 키를 사용
    private String kakaoApiKey = "b37675fa5cc9cc6aa00018901eded22e";

    private final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";

    public String getAddressFromCoordinates(double latitude, double longitude) throws JSONException {
        // API URL 생성
        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
                .queryParam("x", longitude) // 경도 값
                .queryParam("y", latitude) // 위도 값
                .queryParam("input_coord", "WGS84") // 좌표계 설정
                .toUriString();

        // 헤더 설정 (Authorization)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // JSON 응답에서 주소 추출
        String address = extractAddressFromResponse(response.getBody());
        return address;
    }

    private String extractAddressFromResponse(String responseBody) throws JSONException {
        JSONObject jsonObject = new JSONObject(responseBody);

        // "documents" 배열을 가져옴
        JSONArray documentsArray = jsonObject.getJSONArray("documents");

        // documents 배열이 비어 있지 않다면 첫 번째 요소에서 주소 정보를 추출
        if (documentsArray.length() > 0) {
            JSONObject addressObject = documentsArray.getJSONObject(0).getJSONObject("address");
            return addressObject.getString("address_name");
        } else {
            return "주소 정보를 찾을 수 없습니다.";
        }
    }
}
