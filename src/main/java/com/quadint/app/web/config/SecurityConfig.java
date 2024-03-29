package com.quadint.app.web.config;




import com.quadint.app.web.config.jwt.JwtAuthenticationFilter;
import com.quadint.app.web.config.jwt.JwtAuthorizationFilter;
import com.quadint.app.web.config.jwt.JwtExceptionHandlingFilter;
import com.quadint.app.web.exception.CustomEntryPoint;
import com.quadint.app.web.service.CustomUserDetailService;
import com.quadint.app.web.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final TokenService tokenService;

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.access-token-expired-time-ms}")
    private Long accessTokenExpiredTimeMs;

    @Value("${jwt.refresh-token-expired-time-ms}")
    private Long refreshTokenExpiredTimeMs;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(HttpMethod.GET, "/favorites").hasRole("USER") // 이 patterns 들은 정상적으로 완료되지 않으면 CustomEntryPoint 으로 넘어간다.
                        .mvcMatchers(HttpMethod.POST, "/favorites").hasRole("USER")
                        .anyRequest().permitAll()
                .and()
                        .apply(new MyCustomDsl())
                .and()
                        .exceptionHandling()
                        .authenticationEntryPoint(new CustomEntryPoint())
                .and()
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.
                    addFilter(new JwtAuthenticationFilter(tokenService, authenticationManager, key, accessTokenExpiredTimeMs, refreshTokenExpiredTimeMs))
                    .addFilterBefore(new JwtAuthorizationFilter(key), BasicAuthenticationFilter.class)
                    .addFilterBefore(new JwtExceptionHandlingFilter(key), JwtAuthorizationFilter.class);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
}
