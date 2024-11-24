package coollaafi.wot;

import coollaafi.wot.web.post.WeatherResponse;
import coollaafi.wot.web.post.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void testGetWeatherByCoordinates() {
        WeatherResponse response = weatherService.getWeatherByCoordinates(37.5665, 126.9780);
        assertNotNull(response);
        System.out.println("Description: " + response.getDescription());
        System.out.println("Icon URL: " + response.getIconUrl());
    }
}
