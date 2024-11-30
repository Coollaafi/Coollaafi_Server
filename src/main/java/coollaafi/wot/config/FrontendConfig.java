package coollaafi.wot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FrontendConfig {

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

}
