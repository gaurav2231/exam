package com.questionbank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.questionbank.security.CustomAuthAccessEntryPoint;
import com.questionbank.security.jwtAuthFilter;
import com.questionbank.service.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private jwtAuthFilter jFilter;

	@Autowired
	private UserServiceImpl impl;

	@Autowired
	private CustomAuthAccessEntryPoint entryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.requestMatchers(new AntPathRequestMatcher("/login"), new AntPathRequestMatcher("/register"),
						new AntPathRequestMatcher("/user/add"), new AntPathRequestMatcher("/user-login"),
						new AntPathRequestMatcher("/css/**"), new AntPathRequestMatcher("/js/**"),
						new AntPathRequestMatcher("/images/**"), new AntPathRequestMatcher("/webjars/**"),
						new AntPathRequestMatcher("/favicon.ico"))
				.permitAll().anyRequest().authenticated().and().httpBasic().authenticationEntryPoint(entryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().formLogin()
				.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/", true)
				.failureUrl("/login?error")
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout");
		http.addFilterBefore(jFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(impl).passwordEncoder(encoder());
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
