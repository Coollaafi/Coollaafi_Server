package coollaafi.wot.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

public class CorsConfig implements WebMvcConfigurer {

	public static CorsConfigurationSource apiConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		ArrayList<String> allowedOriginPatterns = new ArrayList<>();
		allowedOriginPatterns.add("http://localhost:8080");
		allowedOriginPatterns.add("http://localhost:3000");
		//        allowedOriginPatterns.add("{프론트 배포 URL}");

		ArrayList<String> allowedHttpMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));

		configuration.setAllowedOrigins(allowedOriginPatterns);
		configuration.setAllowedMethods(allowedHttpMethods);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
