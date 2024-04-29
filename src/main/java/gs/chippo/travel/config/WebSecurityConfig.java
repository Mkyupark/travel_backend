package gs.chippo.travel.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// CSRF 보호 기능 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/auth/**").permitAll()  // 공개 API 경로는 인증을 요구하지 않음
                        .requestMatchers("/api/private/**").authenticated()  // 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()  // 그 외의 모든 요청은 인증이 필요
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JwtTokenFilter를 Security 체인에 추가
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 사용하지 않도록 설정

        return http.build();
    }
}
