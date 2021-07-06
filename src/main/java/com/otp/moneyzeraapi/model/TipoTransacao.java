package com.otp.moneyzeraapi.model;

import javax.persistence.*;

@Entity
@Table(name = "tipo_transacao")
public class TipoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
}
