package com.ecommerceapi.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()

                .requestMatchers("/swagger-ui/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/produtos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/produtos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN")

                .requestMatchers( HttpMethod.POST,"/usuarios").permitAll()
                .requestMatchers( HttpMethod.GET,"/usuarios").hasAnyRole("ADMIN","USER")
                .requestMatchers( HttpMethod.PUT,"/usuarios").hasAnyRole("ADMIN","USER")
                .requestMatchers( HttpMethod.DELETE,"/usuarios").hasAnyRole("ADMIN","USER")
                .requestMatchers( "/usuarios/**").hasRole("ADMIN")

                .requestMatchers( "/itens-carrinho/**").hasRole("USER")

                .anyRequest().permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
