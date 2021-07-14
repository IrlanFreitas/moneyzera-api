package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@DataJpaTest - Poderia ser utilizado ?
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //! Caminhos básicos do teste
        //! Cenário -> Ação / Execução -> Verificação

        //* * Cenário
        final Usuario teste = Usuario.builder().nome("teste").email("teste@email.com").build();
        repository.save(teste);

        //* * Ação / Execução
        final Boolean isEmail = repository.existsByEmail("teste@email.com");

        //* * Verificação
        assertTrue(isEmail);

    }

    @Test
    public void deveRetornarFalsoQuandoNaoOuverUsuarioCadastradoComEmail() {

        //! Cenário
        repository.deleteAll();

        final Boolean existsByEmail = repository.existsByEmail("teste@email.com");

        assertFalse(existsByEmail);

    }

}