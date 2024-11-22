package coollaafi.wot.web.weatherData;

import coollaafi.wot.apiPayload.code.BaseErrorCode;
import coollaafi.wot.apiPayload.exception.GeneralException;

public class WeatherDataHandler extends GeneralException {
    public WeatherDataHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
