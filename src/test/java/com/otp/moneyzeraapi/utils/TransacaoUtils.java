package com.otp.moneyzeraapi.utils;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import com.otp.moneyzeraapi.enums.TipoCategoria;
import com.otp.moneyzeraapi.model.Categoria;
import com.otp.moneyzeraapi.model.Conta;
import com.otp.moneyzeraapi.model.Transacao;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.repository.CategoriaRepository;
import com.otp.moneyzeraapi.repository.ContaRepository;
import com.otp.moneyzeraapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Service
public class TransacaoUtils {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Transacao criarTransacao() {
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
