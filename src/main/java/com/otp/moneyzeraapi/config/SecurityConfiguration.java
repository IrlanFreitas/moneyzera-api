package com.otp.moneyzeraapi.config;

import com.otp.moneyzeraapi.filter.JwtTokenFilter;
import com.otp.moneyzeraapi.service.impl.SecurityUserDetailsServiceImpl;
import com.otp.moneyzeraapi.service.interfaces.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtService jwtService;

    //TODO Uma função injetada pelo Spring?
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtService, userDetailsService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        String senhaCodificada = passwordEncoder().encode("123456");

        // ! Configuração de autenticação em memória - Primeira Versão de Autenticação
//        auth
//                .inMemoryAuthentication()
//                .withUser("user")
//                .password(senhaCodificada)
//                .roles("USER");

        // ! Usando login e senha do usuário
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/usuario/autenticar").permitAll()
                .antMatchers(HttpMethod.POST, "/api/usuario").permitAll()
                .anyRequest().authenticated() //! Todas as outras url precisarão de autenticação
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //! Nenhum estado fica salvo na sessão
                .and().addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); //! Organizando ordem dos filtros.
    }
}
