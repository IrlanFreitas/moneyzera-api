package com.otp.moneyzeraapi.service.interfaces;

import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.impl.UsuarioServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp() {
        service = new UsuarioServiceImpl(repository);
    }

    @Test
    public void deveAutenticarUsuarioCorretamente() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(gerarUsuario()));

        final Usuario usuario = service.autenticar("usuario@email.com", "senha");

        assertNotNull(usuario);
    }

    @Test
    public void deveLancarErroQuandoErrarASenhaNaAutenticacao() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(gerarUsuario()));

        assertThrows(ErroAutenticacao.class, () -> {
            final Usuario usuario = service.autenticar("usuario@email.com", "senha1");
        });
    }

    @Test
    public void deveLancarErroQuandoUsuarioNaoEncontrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(gerarUsuario()));

        assertThrows(ErroAutenticacao.class, () -> {
            final Usuario usuario = service.autenticar("usuarioInexistente@email.com", "dasdasd");
        });
    }

    @Test
    public void deveValidarEmail() {

        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //! Cenário
        repository.deleteAll();

        //! Execução e Verificação
        assertDoesNotThrow(() -> service.validarEmail("teste@email.com"));

    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){

        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> service.validarEmail("teste@email.com"));
    }

    public static Usuario gerarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .id(1L)
                .build();
    }
}