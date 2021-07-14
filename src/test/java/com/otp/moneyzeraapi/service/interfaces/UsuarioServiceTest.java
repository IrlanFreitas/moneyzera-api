package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test()
    public void deveValidarEmail() {

        //! Cenário
        repository.deleteAll();

        //! Execução e Verificação
        assertDoesNotThrow(() -> service.validarEmail("teste@email.com"));

    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){

        final Usuario teste = Usuario.builder().nome("teste").email("teste@email.com").build();
        repository.save(teste);

        assertThrows(RegraNegocioException.class, () -> service.validarEmail("teste@email.com"));
    }

}