package coollaafi.wot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	// url : http://localhost:8080/swagger-ui/index.html#/
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI api() {
		Server server = new Server().url("/");

		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
			.components(authSetting())
			.info(getSwaggerInfo())
			.addServersItem(server);
	}

	private Info getSwaggerInfo() {
		License license = new License();
		license.setName("{wot}");

		return new Info()
			.title("wot API Document")
			.description("wot의 API 문서 입니다.")
			.version("v1.0")
			.license(license);
	}

	private Components authSetting() {

		return new Components()
			.addSecuritySchemes(SECURITY_SCHEME_NAME,
				new SecurityScheme()
					.name(SECURITY_SCHEME_NAME)
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT"));
	}
}

