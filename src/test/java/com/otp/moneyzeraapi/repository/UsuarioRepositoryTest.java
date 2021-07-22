package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.utils.UsuarioUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest - Não precisa subir uma aplicação inteira para testar
@ExtendWith(SpringExtension.class)
@DataJpaTest // Sempre faz rollback ao final das transações de cada teste
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Para não sobrescresver as configurações de banco em memória
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //! Caminhos básicos do teste
        //! Cenário -> Ação / Execução -> Verificação

        //* * Cenário
        final Usuario usuario = UsuarioUtils.gerarUsuarioSemId();
//        repository.save(teste);
        entityManager.persist(usuario);

        //* * Ação / Execução
        final Boolean isEmail = repository.existsByEmail("usuario@email.com");

        //* * Verificação
        assertTrue(isEmail);

    }

    @Test
    public void deveRetornarFalsoQuandoNaoOuverUsuarioCadastradoComEmail() {

        //! Cenário
        // Não precisa mais por conta da anotação @DataJpaTest que cria
        // Uma transação por teste e faz rollback assim que termina
//        repository.deleteAll();

        final Boolean existsByEmail = repository.existsByEmail("teste@email.com");

        assertFalse(existsByEmail);

    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //Cenário
        Usuario usuario = UsuarioUtils.gerarUsuarioSemId();

        //Execução
        final Usuario usuarioSalvo = repository.save(usuario);

        //Validação
        assertNotNull(usuarioSalvo.getId());

    }

    @Test
    public void deveBuscarUmUsuarioPorEmail() {
        Usuario usuario = UsuarioUtils.gerarUsuarioSemId();

        entityManager.persist(usuario);

        final Optional<Usuario> usuarioObtido = repository.findByEmail("usuario@email.com");

        assertTrue(usuarioObtido.isPresent());
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {

        final Optional<Usuario> usuarioObtido = repository.findByEmail("usuario@email.com");

        assertFalse(usuarioObtido.isPresent());
    }

}