package com.serjn.online.config;


import com.serjn.online.sevices.ClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    ClientDetailService clientDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws
            Exception {
        return httpSecurity.authorizeHttpRequests(registry ->
                {
                    registry.requestMatchers("/", "/register").permitAll();
                    registry.requestMatchers("/categories").hasRole("client");
                    registry.requestMatchers("/adminpage").hasRole("admin");
                    registry.anyRequest().authenticated();
                })
                .formLogin(log -> log
                        .loginPage("/login")
                        .successHandler(new AuthSuccessHandler())
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(2592000)
                        .userDetailsService(clientDetailService))
                .build();


    }

    @Bean
    public UserDetailsService userDetailsService(){
        return clientDetailService;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(clientDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
