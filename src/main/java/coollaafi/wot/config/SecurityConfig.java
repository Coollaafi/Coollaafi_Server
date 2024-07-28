package coollaafi.wot.config;

import coollaafi.wot.jwt.JwtTokenProvider;
import coollaafi.wot.jwt.JwtVerifyFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/login/oauth2/code/kakao", "/test", "/h2-console/**", "/kakao", "/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**","/swagger-resources/**", "/v3/api-docs").permitAll()  // /login URL은 인증 없이 접근 가능
                        .anyRequest().authenticated()           // 나머지 URL은 인증 필요
                )
                .addFilterBefore(new JwtVerifyFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/login")                    // 커스텀 로그인 페이지 설정
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)) // 인증되지 않은 사용자가 보호된 페이지에 접근할 때 401 Unauthorized 에러 반환
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")) // CSRF 보호 비활성화
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin())); // H2 콘솔의 프레임 옵션 허용

        return http.build();
    }
}
