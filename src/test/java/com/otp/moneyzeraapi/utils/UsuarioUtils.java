package com.otp.moneyzeraapi.utils;

import com.otp.moneyzeraapi.model.Usuario;

public class UsuarioUtils {

    private static Usuario.UsuarioBuilder gerarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha");
    }

    public static Usuario gerarUsuarioComId() {
        return gerarUsuario()
                .id(1L)
                .build();
    }

    public static Usuario gerarUsuarioSemId() {
        return gerarUsuario().build();
    }
}
