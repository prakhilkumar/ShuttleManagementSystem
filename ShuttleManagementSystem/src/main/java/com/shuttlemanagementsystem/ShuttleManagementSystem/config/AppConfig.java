package com.shuttlemanagementsystem.ShuttleManagementSystem.config;


import com.shuttlemanagementsystem.ShuttleManagementSystem.exception.JwtAuthenticationEntryPoint;
import com.shuttlemanagementsystem.ShuttleManagementSystem.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	    prePostEnabled = false, securedEnabled = false, jsr250Enabled = true
	)
public class AppConfig {

	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Value("${whitelist.auth.urls}")
	private String whiteListAuthUrls[];
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // with new spring security 5
	}	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("whiteListAuthUrls --> "+Arrays.toString(whiteListAuthUrls));
		http
		.csrf().disable()
		.authorizeHttpRequests((requests) -> {
			try {
				requests
				.antMatchers(whiteListAuthUrls)
				.permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	


}
