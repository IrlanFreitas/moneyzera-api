package com.otp.moneyzeraapi.filter;

import com.otp.moneyzeraapi.service.impl.SecurityUserDetailsServiceImpl;
import com.otp.moneyzeraapi.service.interfaces.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SecurityUserDetailsServiceImpl securityUserDetailsService;

    public JwtTokenFilter(JwtService jwtService, SecurityUserDetailsServiceImpl securityUserDetailsService) {
        this.jwtService = jwtService;
        this.securityUserDetailsService = securityUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            final String token = authorization.split(" ")[1];
            final boolean tokenValido = jwtService.isTokenValido(token);

            if (tokenValido) {
                final String loginUsuario = jwtService.obterLoginUsuario(token);

                //! O tipo básico de usuário do Spring
                final UserDetails userDetails = securityUserDetailsService.loadUserByUsername(loginUsuario);

                //! Autenticação necessária do Spring
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, null);

                //! Criando uma autenticação para jogar dentro do contexto Spring
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //! Colocando o usuário na autenticação
                SecurityContextHolder.getContext().setAuthentication(user);

            }
        }

        filterChain.doFilter(request, response);
    }
}
