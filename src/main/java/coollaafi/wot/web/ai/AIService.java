package coollaafi.wot.web.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import coollaafi.wot.web.post.Category;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    private static final double DEFAULT_LAT = 37.5665;  // 서울
    private static final double DEFAULT_LON = 126.9780; // 서울
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${external-api.ai-api.url}")
    private String baseUrl;

    @Transactional
    public String callSegmentApi(MultipartFile image, Set<Category> categorySet) throws Exception {
        // Prepare category list
        String classes = categorySet.stream()
                .map(Category::getAIName)
                .collect(Collectors.joining(","));

        // Build request body
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("image",
                new MultipartInputStreamFileResource(image.getInputStream(), image.getOriginalFilename()));
        requestBody.add("classes", classes);
        log.info(classes);
        return makePostRequest(baseUrl + "/segment", requestBody);
    }

    @Transactional
    public String callAddWeatherApi(LocalDateTime date, double latitude, double longitude) {
        String formattedDate = formatDate(date);

        String apiUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/add_weather")
                .queryParam("date", formattedDate)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .toUriString();

        return makePostRequest(apiUrl);
    }

    @Transactional
    public String getRecommendedOutfit(Long memberId, double lat, double lon) {
        String date = formatDate(LocalDateTime.now());
        String apiUrl = buildApiUrl(memberId, date, lat, lon);

        try {
            return makePostRequest(apiUrl);
        } catch (RuntimeException e) {
            if (isWeatherDataNotFoundError(e)) {
                log.warn("Weather data not found. Retrying with default coordinates...");
                return retryWithDefaultCoordinates(memberId, date);
            }
            throw e;
        }
    }

    private boolean isWeatherDataNotFoundError(RuntimeException e) {
        try {
            JsonNode rootNode = objectMapper.readTree(e.getMessage());
            String detailMessage = rootNode.path("result").asText();
            return detailMessage.contains("날씨 정보를 찾을 수 없습니다.");
        } catch (Exception parseException) {
            log.warn("Failed to parse error message: {}", e.getMessage());
            return false;
        }
    }

    private String buildApiUrl(Long memberId, String date, double lat, double lon) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/recommend_outfit")
                .queryParam("member_id", memberId)
                .queryParam("date", date)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .toUriString();
    }

    private String extractErrorDetail(String errorMessage) {
        try {
            JsonNode rootNode = objectMapper.readTree(errorMessage);
            return rootNode.get("detail").asText();
        } catch (Exception e) {
            log.warn("Failed to parse error message: {}", errorMessage);
            return errorMessage; // 파싱 실패 시 원본 메시지를 반환
        }
    }

    private String retryWithDefaultCoordinates(Long memberId, String date) {
        String defaultApiUrl = buildApiUrl(memberId, date, DEFAULT_LAT, DEFAULT_LON);
        return makePostRequest(defaultApiUrl);
    }


    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    @Transactional
    public List<String> parseApiResponse(String apiResponse) {
        return parseJsonArray(apiResponse, "urls");
    }

    @Transactional
    public Long parseWeatherResponse(String apiResponse) {
        try {
            log.debug("Parsing weather API response: {}", apiResponse);

            JsonNode rootNode = objectMapper.readTree(apiResponse);
            JsonNode insertedIdNode = rootNode.get("inserted_id");

            if (insertedIdNode == null || insertedIdNode.isNull()) {
                throw new RuntimeException("Missing 'inserted_id' in response: " + apiResponse);
            }

            return insertedIdNode.asLong();
        } catch (Exception e) {
            log.error("Failed to parse weather API response: {}", apiResponse, e);
            throw new RuntimeException("Failed to parse weather API response", e);
        }
    }

    @Transactional
    public List<String> parseRecommendedOutfit(String apiResponse) {
        return parseJsonArray(apiResponse, "Similar_Dates");
    }

    private List<String> parseJsonArray(String apiResponse, String fieldName) {
        try {
            log.debug("Parsing weather API response: {}", apiResponse);
            JsonNode rootNode = objectMapper.readTree(apiResponse);
            JsonNode arrayNode = rootNode.get(fieldName);

            List<String> values = new ArrayList<>();
            if (arrayNode != null && arrayNode.isArray()) {
                arrayNode.forEach(node -> values.add(node.asText()));
            }
            return values;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON field: " + fieldName, e);
        }
    }

    private String makePostRequest(String endpoint, MultiValueMap<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to call API: " + response.getStatusCode());
        }
    }

    private String makePostRequest(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, requestEntity,
                    String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to call API: " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Error calling API at endpoint: {}. Exception: {}", endpoint, e.getMessage());
            throw new RuntimeException("Failed to call API", e);
        }
    }
}

