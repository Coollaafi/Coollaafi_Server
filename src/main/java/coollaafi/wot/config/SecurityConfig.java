package coollaafi.wot.config;

import coollaafi.wot.web.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/login/**", "/oauth2/**", "/test", "/swagger-ui/**",
                                "/v3/api-docs/**", "/swagger-resources/**", "/kakao/logout/withAccount",
                                "/v3/api-docs", "/kakao", "/logout") // 인증 없이 접근 가능하도록 설정된 URL들
                        .permitAll()
                        .anyRequest().authenticated() // 그 외의 모든 URL은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 사용자 지정 로그인 페이지 경로
                        .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                        )
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)) // 인증 실패 시 401 반환
                )
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                        .invalidateHttpSession(true) // 세션 무효화
                );

        return http.build();
    }
}
