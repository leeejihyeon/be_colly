package lab.coder.colly.shared.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableConfigurationProperties({
        AuthJwtProperties.class,
        AuthSocialProperties.class
})
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthAuthenticationEntryPoint authAuthenticationEntryPoint,
            AuthAccessDeniedHandler authAccessDeniedHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authAuthenticationEntryPoint)
                        .accessDeniedHandler(authAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/magic-link/**").permitAll()
                        .requestMatchers("/api/auth/token/refresh").permitAll()
                        .requestMatchers("/api/auth/signout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/social/google").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/social/apple").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/auth/signout-all").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/community/posts").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/community/posts").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/community/posts/*/join").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/community/joins/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/community/reports").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/community/restrictions/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/accommodations").permitAll()
                        .requestMatchers("/api/stays/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());

        return http.build();
    }
}
