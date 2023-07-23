package com.ecommerceapi.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()

                .requestMatchers("/swagger-ui/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/produtos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/produtos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN")

                .requestMatchers( HttpMethod.POST,"/usuarios").anonymous()
                .requestMatchers( HttpMethod.GET,"/usuarios").hasAnyRole("ADMIN","USER")
                .requestMatchers( HttpMethod.PUT,"/usuarios").hasRole("USER")
                .requestMatchers( HttpMethod.DELETE,"/usuarios").hasRole("USER")
                .requestMatchers( "/usuarios/**").hasRole("ADMIN")

                .requestMatchers( "/itens-carrinho/**").hasRole("USER")

                .anyRequest().authenticated()
                .and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
