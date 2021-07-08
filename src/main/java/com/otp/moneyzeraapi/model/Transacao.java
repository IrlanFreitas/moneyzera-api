package com.otp.moneyzeraapi.model;

import com.otp.moneyzeraapi.enums.StatusTransacao;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacao")
@Data
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    private BigDecimal valor;

//    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate data;
}
