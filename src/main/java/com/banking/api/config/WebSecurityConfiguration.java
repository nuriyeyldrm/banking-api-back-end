package com.banking.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/customers/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/employees/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/accounts/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/transfers/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/customers/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/employees/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/accounts/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/transfers/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/users/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/customers/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/employees/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/accounts/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/transfers/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/users/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/customers/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/employees/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/accounts/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/transfers/**").permitAll()
                .anyRequest().authenticated();
    }
}
