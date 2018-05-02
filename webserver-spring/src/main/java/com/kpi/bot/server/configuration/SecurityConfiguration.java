package com.kpi.bot.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String REALM_NAME = "main";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password("{noop}QW123456").roles("ADMIN"); //or authority ROLE_ADMIN
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and().httpBasic().realmName(REALM_NAME)//.authenticationEntryPoint(new AuthenticationEntryPoint())

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().csrf().disable();
    }
}
