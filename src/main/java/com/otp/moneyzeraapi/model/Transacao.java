package com.otp.moneyzeraapi.model;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "tipo_transacao_id")
    private TipoTransacao tipoTransacao;

    @ManyToOne
    @JoinColumn(name = "status_transacao_id")
    private StatusTransacao statusTransacao;

    private BigDecimal valor;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate data;
}
