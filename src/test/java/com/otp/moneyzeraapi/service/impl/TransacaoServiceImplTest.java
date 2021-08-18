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
import com.otp.moneyzeraapi.service.interfaces.ContaService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransacaoServiceImplTest {

    // ! É da classe que estamos testando pois é necessário
    // ! que chame os métodos reais
    @SpyBean
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

    @Disabled
    @Test
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

    @Disabled
    @Test
    public void naoDeveSalvarUmaTransacaoQuandoHouverErroDeValidacao() {
        final Transacao transacaoASalvar = criarTransacao();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(transacaoASalvar);

        assertThrows(RegraNegocioException.class, () -> service.salvar(transacaoASalvar));

        Mockito.verify(repository, Mockito.never()).save(transacaoASalvar);
    }

    @Disabled
    @Test
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

    @Disabled
    @Test
    public void deveLancarErroAoTentarAtualizarUmaTransacaoQueAindaNaoFoiSalva() {

        // * Cenário
        Transacao transacaoSalva = criarTransacao();

        // * Execução e Verificação
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(transacaoSalva));
        Mockito.verify(repository, Mockito.never()).save(transacaoSalva);
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