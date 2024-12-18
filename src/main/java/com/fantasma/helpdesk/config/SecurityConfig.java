package com.fantasma.helpdesk.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fantasma.helpdesk.security.JWTAuthenticationFilter;
import com.fantasma.helpdesk.security.JWTAuthorizationFilter;
import com.fantasma.helpdesk.security.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
 
    //@Autowired
    public SecurityConfig(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtil))
                .addFilter(new JWTAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService))
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.getOrBuild();
    }
    
    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    //}
 
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/h2-console/**")
        );
    }
 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://helpdesk-fantasma.netlify.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        
        // Use CorsConfigurationSource em vez de UrlBasedCorsConfigurationSource
        org.springframework.web.cors.CorsConfigurationSource source = new org.springframework.web.cors.CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                return configuration;
            }
        };
        
        return source;
    }
 
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
