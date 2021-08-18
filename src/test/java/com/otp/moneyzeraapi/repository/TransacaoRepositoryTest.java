package com.otp.moneyzeraapi.repository;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransacaoRepositoryTest {

    @Autowired
    private TransacaoRepository repository;

//    @Autowired
//    private TransacaoUtils utils;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void deveSalvarUmaTransacao() {
        //TODO Necessário mesmo criar todas a entidades dependentes para testar integrado ?
        //TODO uma forma de facilitar isso

        Transacao transacao = criarTransacao();

        transacao = repository.save(transacao);

        assertNotNull(transacao.getId());

    }

    @Test
    public void deveDeletarUmaTransacao() {

        Transacao transacao = criarTransacao();

        entityManager.persist(transacao);

        transacao = entityManager.find(Transacao.class, transacao.getId());

        repository.delete(transacao);

        Transacao transacaoInexistente = entityManager.find(Transacao.class, transacao.getId());

        assertNull(transacaoInexistente);
    }

    @Test
    public void deveAtualizarUmaTransacao() {
        Transacao transacao = criarTransacao();
        transacao = entityManager.persist(transacao);

        transacao.setData(LocalDate.of(2020, 1, 20));
        transacao.setNome("Livro de Idiomas");
        transacao.setDescricao("Everything about another language");

        repository.save(transacao);

        Transacao transacaoAtualizada = entityManager.find(Transacao.class, transacao.getId());

        assertEquals(transacaoAtualizada.getNome(), "Livro de Idiomas");
        assertEquals(transacaoAtualizada.getDescricao(), "Everything about another language");
        assertEquals(transacaoAtualizada.getData(), LocalDate.of(2020, 1, 20));
    }

    @Test
    public void deveBuscarUmaTransacaoPorId() {
        Transacao transacao = entityManager.persist(criarTransacao());

        Optional<Transacao> transacaoEncontrada = repository.findById(transacao.getId());

        assertTrue(transacaoEncontrada.isPresent());
    }

    // TODO Tive que repetir esse método pois não encontrei uma forma de utilizar
    // * tentativa frustrada de colocar isso tudo numa classe utilitária
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