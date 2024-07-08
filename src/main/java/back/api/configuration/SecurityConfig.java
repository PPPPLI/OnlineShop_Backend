package back.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(Oauth2 -> Oauth2.jwt(Customizer.withDefaults()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority("SCOPE_ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST, "/users/unblock/**", "/users/block/**").hasAnyAuthority("SCOPE_ROLE_ADMIN")


                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/resources/images/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/auth/test").hasAnyAuthority("SCOPE_ROLE_ADMIN")

                    .requestMatchers(HttpMethod.GET,"/products/**", "/products").permitAll()
                    .requestMatchers(HttpMethod.POST, "/products/**", "/products").hasAnyAuthority("SCOPE_ROLE_ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/products/**", "/products").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/products/**", "/products").hasAnyAuthority("SCOPE_ROLE_ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/**", "/products").hasAnyAuthority("SCOPE_ROLE_ADMIN")
                    .anyRequest().denyAll())
            .build();
    }

}
