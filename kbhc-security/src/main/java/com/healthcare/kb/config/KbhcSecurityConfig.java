package com.healthcare.kb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.kb.dto.AuthErrorResponse;
import com.healthcare.kb.filter.JwtAuthenticationFilter;
import com.healthcare.kb.service.KbhcSecurityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.healthcare.kb.type.RoleType.ADMIN;
import static com.healthcare.kb.type.RoleType.EXPERT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class KbhcSecurityConfig {

    @Value("${secret.key.salt}")
    private String salt;

    @Value("${secret.key.enc-passwrd}")
    private String symmetricKey;

    private final KbhcSecurityService kbhcSecurityService;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public BytesEncryptor aesBytesEncryptor() {
        return Encryptors.stronger(symmetricKey, salt);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(kbhcSecurityService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        //https://non-stop.tistory.com/667
        //https://velog.io/@park2348190/Spring-Security%EC%9D%98-Unauthorized-Forbidden-%EC%B2%98%EB%A6%AC
        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(unauthorizedEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(
                        config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        registry ->
                                registry
                                        .requestMatchers(
                                                AntPathRequestMatcher.antMatcher("/"),
                                                AntPathRequestMatcher.antMatcher("/kbhc/swagger/**"),
                                                AntPathRequestMatcher.antMatcher("/h2-console/**"),

                                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/kbhc/user/signup"),
                                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/kbhc/user/login"),
                                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/kbhc/user/refresh-token")
                                        ).permitAll()
                                        .requestMatchers(
                                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/kbhc/**/comment"),
                                                AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/kbhc/**/comment/**"),
                                                AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/kbhc/**/comment/**")
                                        )
                                        .hasRole(EXPERT.name())
                                        .requestMatchers(
                                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/kbhc/user/change-role")
                                        )
                                        .hasRole(ADMIN.name())
                                        .anyRequest()
                                        .authenticated()
                )
        ;
        http.addFilterAfter(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Refresh-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //403 Forbidden
    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, authException) -> {
                AuthErrorResponse<Void> errorResponse = AuthErrorResponse.forbidden();
                sendResponse(response, errorResponse);
            };

    //401 Unauthorized
    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                AuthErrorResponse<Void> errorResponse = AuthErrorResponse.unauthorized();
                sendResponse(response, errorResponse);
            };


    private void sendResponse(HttpServletResponse response, AuthErrorResponse<Void> errorResponse) throws IOException {
        String jsonErrorResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.setStatus(errorResponse.getResultCode());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);
    }
}
