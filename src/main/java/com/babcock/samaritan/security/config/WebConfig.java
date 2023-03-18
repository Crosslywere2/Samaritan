package com.babcock.samaritan.security.config;

import com.babcock.samaritan.model.Role;
import com.babcock.samaritan.security.JWTFilter;
import com.babcock.samaritan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .httpBasic()
            .disable()
            .cors()
            .and()
            .authorizeHttpRequests()
                .antMatchers("/api/register/student", "/api/student/login", "/api/officer/login", "/api/found").permitAll()
                .antMatchers("/api/student/item/**", "/api/student/info").hasAuthority(Role.STUDENT.name())
                .antMatchers("/api/officer/**").hasAnyAuthority(Role.OFFICER.name(), Role.ADMIN_OFFICER.name())
                .antMatchers("/api/admin/**", "/api/register/officer").hasAuthority(Role.ADMIN_OFFICER.name())
            .and()
            .userDetailsService(userService)
            .exceptionHandling()
                .authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
