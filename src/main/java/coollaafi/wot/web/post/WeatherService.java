package coollaafi.wot.web.post;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class WeatherService {

    @Value("${external-api.weather-api.key}")
    private String key;

    private final WebClient webClient;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openweathermap.org/data/2.5")
                .build();
    }

    public WeatherResponse getWeatherByCoordinates(double lat, double lon) {
        try {
            Map response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", key)
                            .queryParam("units", "metric")
                            .queryParam("lang", "kr")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // 필요한 데이터만 추출
            Map<String, Object> weather = ((Map<String, Object>) ((java.util.List<?>) response.get("weather")).get(0));
            String description = (String) weather.get("description");
            String iconUrl = "https://openweathermap.org/img/wn/" + weather.get("icon") + ".png";

            return new WeatherResponse(description, iconUrl);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("API 호출 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("예기치 않은 오류 발생: " + e.getMessage(), e);
        }
    }

}
