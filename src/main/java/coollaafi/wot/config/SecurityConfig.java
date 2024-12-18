package coollaafi.wot.config;

import coollaafi.wot.jwt.JwtTokenFilter;
import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.web.oauth2.CustomOAuth2UserService;
import coollaafi.wot.web.oauth2.KakaoLogoutHandler;
import coollaafi.wot.web.oauth2.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 제공자 추가
    private final KakaoLogoutHandler kakaoLogoutHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, JwtTokenProvider jwtTokenProvider,
                          KakaoLogoutHandler kakaoLogoutHandler,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoLogoutHandler = kakaoLogoutHandler;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(
                List.of("https://coollaafi-frontend.vercel.app", "http://localhost:3000", "https://coollaafi.shop"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/member/healthcheck", "/login/**", "/oauth2/**", "/test",
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/kakao/logout/withAccount", "/kakao", "/logout",
                                "/auth/refresh") // 인증 없이 접근 가능하도록 설정된 URL들
                        .permitAll()
                        .anyRequest().authenticated() // 그 외의 모든 URL은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler) // 성공 시 커스텀 핸들러
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                        )
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)) // 인증 실패 시 401 반환
                )
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                        corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .addLogoutHandler(kakaoLogoutHandler) // Kakao 로그아웃 처리 추가
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                        .invalidateHttpSession(true) // 세션 무효화
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Logout successful\"}");
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
