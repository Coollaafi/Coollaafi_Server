package coollaafi.wot.web.weatherData;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    public WeatherDataService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public WeatherData fetchWeatherDataById(Long weatherDataId) {
        return weatherDataRepository.findById(weatherDataId)
                .orElseThrow(() -> new RuntimeException("WeatherData not found for ID: " + weatherDataId));
    }
}

