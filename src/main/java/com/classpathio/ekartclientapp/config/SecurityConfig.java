package com.classpathio.ekartclientapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login/**", "/logout/**", "/contact-us**").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oath2Login -> {
                oath2Login
                    .redirectionEndpoint(redirectionEndpoint ->
                        redirectionEndpoint
                            .baseUri("/authorization-code/callback")
                    );
            })
                .cors(Customizer.withDefaults())
                .csrf(Customizer.withDefaults());
        return http.build();
    }
}
