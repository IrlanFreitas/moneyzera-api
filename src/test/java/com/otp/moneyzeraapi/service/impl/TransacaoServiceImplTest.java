package com.otp.moneyzeraapi.service.impl;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.CategoriaRepository;
import com.otp.moneyzeraapi.repository.ContaRepository;
import com.otp.moneyzeraapi.repository.TransacaoRepository;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransacaoServiceImplTest {

    // ! É da classe que estamos testando pois é necessário
    // ! que chame os métodos reais
    @SpyBean
//    @Autowired
    private TransacaoServiceImpl service;

    // ! Todas as chamadas são mocadas (simuladas)
    @MockBean
    private TransacaoRepository repository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private ContaRepository contaRepository;

    @MockBean
    private CategoriaRepository categoriaRepository;

    @Test
    @Disabled
    public void deveSalvarUmaTransacao() {
        // ! Tem algo relacionado ao ContaService que eu preciso Mockar

        // * Parece ser um teste de chamada de método somente


        // * Cenário
        final Transacao transacaoASalvar = criarTransacao();
        Mockito.doNothing().when(service).validar(transacaoASalvar);
//        Mockito.doNothing().when(service).movimentar(transacaoASalvar);

        Transacao transacaoSalva = criarTransacao();
        transacaoSalva.setId(1L);
        Mockito.when(repository.save(transacaoASalvar)).thenReturn(transacaoSalva);

        // * Execução
        final Transacao transacao = service.salvar(transacaoASalvar);

        // * Verificação
        assertEquals(transacaoSalva.getId(), transacao.getId());

    }

    @Test
    @Disabled
    public void naoDeveSalvarUmaTransacaoQuandoHouverErroDeValidacao() {
        final Transacao transacaoASalvar = criarTransacao();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(transacaoASalvar);

        assertThrows(RegraNegocioException.class, () -> service.salvar(transacaoASalvar));

        Mockito.verify(repository, Mockito.never()).save(transacaoASalvar);
    }

    @Test
    @Disabled
    public void deveAtualizarUmaTransacao() {

        // * Cenário
        Transacao transacaoSalva = criarTransacao();
        transacaoSalva.setId(1L);
        Mockito.doNothing().when(service).validar(transacaoSalva);
        Mockito.when(repository.save(transacaoSalva)).thenReturn(transacaoSalva);

        // * Execução
        service.atualizar(transacaoSalva);

        // * Verificação
        Mockito.verify(repository, Mockito.times(1)).save(transacaoSalva);
    }

    @Test
    @Disabled
    public void deveLancarErroAoTentarAtualizarUmaTransacaoQueAindaNaoFoiSalva() {

        // * Cenário
        Transacao transacaoSalva = criarTransacao();

        // * Execução e Verificação
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(transacaoSalva));
        Mockito.verify(repository, Mockito.never()).save(transacaoSalva);
    }

    @Test
    public void deveDeletarUmaTransacao() {
        // * Cenário
        Transacao transacao = criarTransacao();
        transacao.setId(1L);

        // * Execução
        service.deletar(1L);

        // * Verificação
        Mockito.verify(repository).deleteById(1L);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmaTransacaoQueAindaNaoFoiSalva() {
        // * Cenário
        Transacao transacao = criarTransacao();

        // * Execução
        assertThrows(IllegalArgumentException.class, () -> service.deletar(1L));

        // * Verificação
        Mockito.verify(repository, Mockito.never()).deleteById(1L);
    }

    @Test
    public void deveFiltrarTransacoes() {
        Transacao transacao = criarTransacao();
        transacao.setId(1L);

        List<Transacao> transacoes = Arrays.asList(transacao);
        Mockito.when(repository.findAll(Mockito.any(Example.class)) ).thenReturn(transacoes);

        List<Transacao> resultado = service.buscar(transacao);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());

    }

    @Test
    public void deveObterUmaTransacaoPorId() {
        Transacao transacao = criarTransacao();
        transacao.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(transacao));

        final Optional<Transacao> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    public void deveRetornarVazioQuandoTransacaoNaoExiste() {

        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        final Optional<Transacao> resultado = service.buscarPorId(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void deveLancarErrorAoValidar() {
        Transacao transacao = Transacao.builder().build();

        final RegraNegocioException descricaoException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe uma descrição válida.", descricaoException.getMessage());
        
        transacao.setDescricao("Descrição");

        final RegraNegocioException dataException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe uma data válida.", dataException.getMessage());

        transacao.setData(LocalDate.of(2002, 2, 14));

        final RegraNegocioException contaOrigemException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe uma conta já cadastrada.", contaOrigemException.getMessage());

        Conta contaOrigem = Conta.builder().id(1L).build();

        transacao.setContaOrigem(contaOrigem);

        final RegraNegocioException contaOrigemUsuarioException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe um usuário já cadastrado.", contaOrigemUsuarioException.getMessage());

        contaOrigem.setUsuario(Usuario.builder().id(1L).build());
        transacao.setCategoria(Categoria.builder().tipo(TipoCategoria.TRANSFERENCIA).build());

        final RegraNegocioException contaDestinoException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe uma conta destino já cadastrada.", contaDestinoException.getMessage());

        Conta contaDestino = Conta.builder().id(2L).build();
        contaDestino.setUsuario(Usuario.builder().id(2L).build());
        transacao.setContaDestino(contaDestino);

        final RegraNegocioException contaDestinoUsuarioException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe um usuário na conta destino já cadastrado.", contaDestinoUsuarioException.getMessage());

        transacao.setValor(BigDecimal.ZERO);

        final RegraNegocioException valorException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe um valor válido.", valorException.getMessage());

        transacao.setCategoria(null);

        final RegraNegocioException categoriaException = assertThrows(RegraNegocioException.class, () -> service.validar(transacao));
        assertEquals("Informe uma categoria.", categoriaException.getMessage());

        transacao.setCategoria(Categoria.builder().tipo(TipoCategoria.TRANSFERENCIA).build());

        assertDoesNotThrow(() -> service.validar(transacao));


    }

    @Test
    public void deveAtualizarStatus() {
        Transacao transacao = criarTransacao();
        transacao.setId(1L);
        transacao.setStatus(StatusTransacao.EFETIVADO);

        Mockito.doNothing().when(service).atualizar(transacao);

        service.atualizarStatus(transacao, StatusTransacao.PENDENTE);

        assertEquals(transacao.getStatus().name(), StatusTransacao.PENDENTE.name());
        Mockito.verify(service).atualizar(transacao);
    }

    private Transacao criarTransacao() {
        Usuario usuario = Usuario.builder()
                .nome("Irlan")
                .email("irlan.freitas@teste.com")
                .senha("teste@123")
                .data(LocalDate.now())
                .build();

        usuario = usuarioRepository.save(usuario);

        Conta conta = Conta.builder()
                .usuario(usuario)
                .nome("Neon")
                .descricao("Mais uma conta digital")
                .build();

        conta = contaRepository.save(conta);

        Categoria categoria = Categoria.builder()
                .tipo(TipoCategoria.RECEITA)
                .nome("Educação")
                .descricao("Livros, cursos, materiais de apoio")
                .build();

        categoria = categoriaRepository.save(categoria);

        return Transacao.builder()
                .nome("Livro de Inglês")
                .descricao("Grammar in Use")
                .contaOrigem(conta)
                .categoria(categoria)
                .status(StatusTransacao.valueOf("EFETIVADO"))
                .valor(BigDecimal.valueOf(22.00))
                .data(LocalDate.of(2021, 7, 13))
                .build();
    }
}