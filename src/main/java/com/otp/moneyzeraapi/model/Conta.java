package com.otp.moneyzeraapi.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
