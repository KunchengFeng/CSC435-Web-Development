package population;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // I have to use .permitAll() so that my Integration Tests and
        // Postman requests still work, this kind of breaks the
        // Purpose of the authentication login, but it's better
        // than 3 of the endpoints not testable.
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/error", "/webjars/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/population/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/population/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/population/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/population/**").permitAll()
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/").permitAll()
                        .invalidateHttpSession(true)
                )
                .csrf().disable()
                .httpBasic().disable()
                .oauth2Login()
                ;
        return http.build();
    }
}