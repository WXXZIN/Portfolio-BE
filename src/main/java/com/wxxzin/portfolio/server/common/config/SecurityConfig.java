package com.wxxzin.portfolio.server.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.wxxzin.portfolio.server.common.auth.security.device.service.DeviceIdExtractor;
import com.wxxzin.portfolio.server.common.auth.security.filter.JsonLoginFilter;
import com.wxxzin.portfolio.server.common.auth.security.filter.JwtAuthenticationFilter;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenExtractor;
import com.wxxzin.portfolio.server.common.auth.security.jwt.service.JwtTokenValidator;
import com.wxxzin.portfolio.server.common.auth.security.local.handler.JsonLoginFailureHandler;
import com.wxxzin.portfolio.server.common.auth.security.local.handler.JsonLoginSuccessHandler;
import com.wxxzin.portfolio.server.common.auth.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String [] allowAllUrl = { "/", "/error", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/health" };
	private static final String [] allowAllAuthUrl = { "/api/v1/user/auth/sdk/oauth2/{provider}", "/api/v1/user/auth/logout", "/api/v1/user/auth/reissue", 
														"/api/v1/user/auth/reissue/social", "/api/v1/user/auth/find-username" };
	private static final String [] allowAllEmailUrl = { "/api/v1/email/**" };
	private static final String [] allowAllUserUrl = {  "/api/v1/user/register", "/api/v1/user/is-username-taken", "/api/v1/user/is-nickname-taken" };
    
	private static final String [] allowAllProjectUrl = { "/api/v1/project", "/ap1/v1/project/search", "/api/v1/project/{projectId}" };

	private static final String [] allowAuthUrl = { "/api/v1/user/auth/change-password", "/api/v1/user/auth/logout/{targetDeviceId}",
													"/api/v1/user/auth/delete" };
	private static final String [] allowDeviceUrl = { "/api/v1/user/device", "/api/v1/user/device/{deviceId}" };
    private static final String [] allowUserUrl = { "/api/v1/user/profile", "/api/v1/user/change-nickname" };
	private static final String [] allowTeamUrl = { "/api/v1/team", "/api/v1/team/{teamId}", "/api/v1/team/{teamId}/change-leader" };
	private static final String [] allowTeamApplicationUrl = { "/api/v1/team/{teamId}/application", "/api/v1/team/{teamId}/application/{teamApplicationId}" };
	private static final String [] allowTeamTaskUrl = { "/api/v1/team/{teamId}/task", "/api/v1/team/{teamId}/task/{taskId}" };
	private static final String [] allowProjectUrl = { "/api/v1/project/hearted" };
	private static final String [] allowHeartUrl = { "/api/v1/project/heart/{projectId}" };

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JwtTokenValidator jwtTokenValidator;
	private final JwtTokenExtractor jwtTokenExtractor;
	private final DeviceIdExtractor deviceIdExtractor;
	private final AuthenticationService authenticationService;
	private final JsonLoginSuccessHandler jsonLoginSuccessHandler;
	private final JsonLoginFailureHandler jsonLoginFailureHandler;

    public SecurityConfig(
		AuthenticationConfiguration authenticationConfiguration,
		JwtTokenValidator jwtTokenValidator,
		JwtTokenExtractor jwtTokenExtractor,
		DeviceIdExtractor deviceIdExtractor,
		AuthenticationService authenticationService,
		JsonLoginSuccessHandler jsonLoginSuccessHandler, 
		JsonLoginFailureHandler jsonLoginFailureHandler
	) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jsonLoginSuccessHandler = jsonLoginSuccessHandler;
		this.jsonLoginFailureHandler = jsonLoginFailureHandler;
		this.jwtTokenValidator = jwtTokenValidator;
		this.jwtTokenExtractor = jwtTokenExtractor;
		this.deviceIdExtractor = deviceIdExtractor;
		this.authenticationService = authenticationService;
	}

	@Bean
    public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.cors((cors) -> cors
				.configurationSource(request -> {
					CorsConfiguration corsConfiguration = new CorsConfiguration();

					corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
					corsConfiguration.setAllowCredentials(true);
					corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Client-Type"));
					corsConfiguration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
					corsConfiguration.setMaxAge(3600L);

					return corsConfiguration;
				})
			);

		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);

		http
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.requestMatchers(allowAllUrl).permitAll()
				.requestMatchers(allowAllAuthUrl).permitAll()
				.requestMatchers(allowAllEmailUrl).permitAll()
				.requestMatchers(allowAllUserUrl).permitAll()
				.requestMatchers(allowAllProjectUrl).permitAll()
				.requestMatchers(allowAuthUrl).authenticated()
				.requestMatchers(allowDeviceUrl).authenticated()
				.requestMatchers(allowUserUrl).authenticated()
				.requestMatchers(allowTeamUrl).authenticated()
				.requestMatchers(allowTeamApplicationUrl).authenticated()
				.requestMatchers(allowTeamTaskUrl).authenticated()
				.requestMatchers(allowProjectUrl).authenticated()
				.requestMatchers(allowHeartUrl).authenticated()
				.anyRequest().denyAll()
			);

		http
			.formLogin(AbstractHttpConfigurer::disable);

		http
			.httpBasic(AbstractHttpConfigurer::disable);

		http
			.logout(AbstractHttpConfigurer::disable);

		http
			.addFilterAt(new JsonLoginFilter(authenticationManager(), jsonLoginSuccessHandler, jsonLoginFailureHandler), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtAuthenticationFilter(authenticationService, jwtTokenValidator, jwtTokenExtractor, deviceIdExtractor), JsonLoginFilter.class);
		
		return http.build();
    }
}
