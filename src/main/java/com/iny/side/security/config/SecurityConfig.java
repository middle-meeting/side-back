package com.iny.side.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iny.side.common.BasicResponse;
import com.iny.side.common.config.CorsConfig;
import com.iny.side.security.entrypoint.RestAuthenticationEntryPoint;
import com.iny.side.security.filter.RestAuthenticationFilter;
import com.iny.side.security.handler.FormAccessDeniedHandler;
import com.iny.side.security.handler.RestAccessDeniedHandler;
import com.iny.side.users.web.dto.LogoutResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationProvider restAuthenticationProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationSuccessHandler restSuccessHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationFailureHandler restFailureHandler;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    private final CorsConfig corsConfig;

    public SecurityConfig(
            @Qualifier("authenticationProvider") AuthenticationProvider authenticationProvider,
            @Qualifier("restAuthenticationProvider") AuthenticationProvider restAuthenticationProvider,
            @Qualifier("successHandler") AuthenticationSuccessHandler successHandler,
            @Qualifier("restSuccessHandler") AuthenticationSuccessHandler restSuccessHandler,
            @Qualifier("failureHandler") AuthenticationFailureHandler failureHandler,
            @Qualifier("restFailureHandler") AuthenticationFailureHandler restFailureHandler,
            AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource, CorsConfig corsConfig) {

        this.authenticationProvider = authenticationProvider;
        this.restAuthenticationProvider = restAuthenticationProvider;
        this.successHandler = successHandler;
        this.restSuccessHandler = restSuccessHandler;
        this.failureHandler = failureHandler;
        this.restFailureHandler = restFailureHandler;
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.corsConfig = corsConfig;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll() // 정적 자원 설정
//                        .requestMatchers("/", "/signup", "/login*").permitAll()
//                        .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")
//                        .requestMatchers("/professor/**").hasAuthority("ROLE_PROFESSOR")
//                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login").permitAll()
//                        .authenticationDetailsSource(authenticationDetailsSource)
//                        .successHandler(successHandler)
//                        .failureHandler(failureHandler)
//                )
//                .authenticationProvider(authenticationProvider)
//                .exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")))
//                .logout(logout -> logout
//                        .logoutSuccessHandler((request, response, authentication) ->
//                                response.sendRedirect("/"))
//                        .deleteCookies("JSESSIONID")
//                )
//        ;
//        return http.build();
//    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository()) // HttpOnly=true (기본값)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                ).addFilter(corsConfig.corsFilter())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll() // 정적 자원 설정
                        .requestMatchers("/api/csrf", "/api/signup/**", "/api/login").permitAll()
                        .requestMatchers("/api/student/**").hasAuthority("ROLE_STUDENT")
                        .requestMatchers("/api/professor/**").hasAuthority("ROLE_PROFESSOR")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler()))
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            ObjectMapper mapper = new ObjectMapper();
                            LogoutResponseDto logoutResponse = LogoutResponseDto.success();
                            BasicResponse<LogoutResponseDto> basicResponse = BasicResponse.ok(logoutResponse);

                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                            mapper.writeValue(response.getWriter(), basicResponse);
                        })
                        .deleteCookies("JSESSIONID")
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }

    private RestAuthenticationFilter restAuthenticationFilter(HttpSecurity http, AuthenticationManager authenticationManager) {
        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(http);
        restAuthenticationFilter.setAuthenticationManager(authenticationManager);
        restAuthenticationFilter.setAuthenticationSuccessHandler(restSuccessHandler);
        restAuthenticationFilter.setAuthenticationFailureHandler(restFailureHandler);
        return restAuthenticationFilter;
    }
}
