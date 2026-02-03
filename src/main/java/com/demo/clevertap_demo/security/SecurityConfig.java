package com.demo.clevertap_demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        // API stateless => CSRF fuera
        .csrf(csrf -> csrf.disable())

        // No sesiones
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Evita login form / redirects
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form.disable())

        .authorizeHttpRequests(auth -> auth

            // Salud / ping (opcional)
            .requestMatchers(HttpMethod.GET, "/ping").permitAll()

            // Auth (registro/login sin token)
            .requestMatchers("/auth/**").permitAll()

            // Swagger / OpenAPI
            .requestMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-ui/index.html",
                "/v3/api-docs",
                "/v3/api-docs/**"
            ).permitAll()

            // Evita Whitelabel de Spring si algo falla
            .requestMatchers("/error").permitAll()

            // lo demás con JWT
            .anyRequest().authenticated()
        )

        // Filtro JWT antes del filtro de username/password
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}



