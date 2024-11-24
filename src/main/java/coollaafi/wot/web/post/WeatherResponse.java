package coollaafi.wot.web.post;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherResponse {
    private String description;
    private String iconUrl;

    public WeatherResponse(String description, String iconUrl) {
        this.description = description;
        this.iconUrl = iconUrl;
    }

}
