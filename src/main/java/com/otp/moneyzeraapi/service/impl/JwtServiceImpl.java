package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiration}")
    private String expiracao;

    @Value("${jwt.assign-key}")
    private String chave;

    @Override
    public String gerarToken(Usuario usuario) {

        long expLong = Long.parseLong(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expLong);
        final Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        final java.util.Date dateExpiracao = Date.from(instant);

        return Jwts
                .builder()
                .setExpiration(dateExpiracao)
                .setSubject(usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("horaExpiracao", dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .signWith(SignatureAlgorithm.HS512, chave)
                .compact();
    }

    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValido(String token) {

        try {
            final Claims claims = obterClaims(token);
            final Date expiration = claims.getExpiration();

            final LocalDateTime localDateTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return LocalDateTime.now().isBefore(localDateTime);
        } catch (ExpiredJwtException exception) {
            return false;
        }
    }

    @Override
    public String obterLoginUsuario(String token) {
        final Claims claims = obterClaims(token);

        return claims.getSubject();
    }
}
