package com.example.testapp;

import com.example.testapp.security.DatabaseUserServiceDetails;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@EnableOAuth2Sso
@Configuration
public class MainConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.logout().logoutSuccessUrl("/").permitAll().and()
			.authorizeRequests().antMatchers("/", "/login", "/home.html").permitAll()
			.anyRequest().authenticated()
			.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		// @formatter:on
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled=true)
	protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService());//.passwordEncoder(new BCryptPasswordEncoder());
		}

		@Bean
		public UserDetailsService userDetailsService() {
			return new DatabaseUserServiceDetails();
		}
	}
}
