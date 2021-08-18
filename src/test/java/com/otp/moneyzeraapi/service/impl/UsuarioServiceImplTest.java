package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import com.otp.moneyzeraapi.service.impl.UsuarioServiceImpl;
import com.otp.moneyzeraapi.utils.UsuarioUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioServiceImplTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

//    @BeforeEach
//    public void setUp() {
//        service = Mockito.spy(UsuarioServiceImpl.class);
//
//        // Mudando a abordagem para usar spy
//        // service = new UsuarioServiceImpl(repository);
//    }

    @Test
    public void deveAutenticarUsuarioCorretamente() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(UsuarioUtils.gerarUsuarioComId()));

        final Usuario usuario = service.autenticar("usuario@email.com", "senha");

        assertNotNull(usuario);
    }

    @Test
    public void deveLancarErroQuandoErrarASenhaNaAutenticacao() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(UsuarioUtils.gerarUsuarioComId()));

        final ErroAutenticacao erroAutenticacao = assertThrows(ErroAutenticacao.class, () -> {
            final Usuario usuario = service.autenticar("usuario@email.com", "senha1");
        });

        assertEquals("Senha inválida.", erroAutenticacao.getMessage());
    }

    @Test
    public void deveLancarErroQuandoUsuarioNaoEncontrado() {
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        final ErroAutenticacao erroAutenticacao = assertThrows(ErroAutenticacao.class, () -> {
            final Usuario usuario = service.autenticar("aushdauishdaiusdh@email.com", "dasdasd");
        });

        assertEquals( "Usuário não encontrado.", erroAutenticacao.getMessage());
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

    @Test
    public void deveLancarErroAoChecarEmailJaCadastrado() {
//        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(Boolean.TRUE);

        final Usuario usuario = UsuarioUtils.gerarUsuarioSemId();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail("usuario@email.com");

        assertThrows( RegraNegocioException.class, () -> service.salvar(usuario));

//        assertEquals("Já existe usuário cadastrado com esse email", regraNegocioException.getMessage());
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }

    @Test
    public void deveCadastrarComSucesso() {
        // ** Foi trocado pelo Spy que é um Mock parcial quando não é mockado
        // Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(Boolean.FALSE);

        Mockito.when(repository.save(Mockito.any())).thenReturn(UsuarioUtils.gerarUsuarioComId());

        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());

        final Usuario usuario = service.salvar(new Usuario());

        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("usuario", usuario.getNome());
        assertEquals("usuario@email.com", usuario.getEmail());
        assertEquals("senha", usuario.getSenha());

    }

}