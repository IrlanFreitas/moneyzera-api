package com.otp.moneyzeraapi.model;

import javax.persistence.*;

@Entity
@Table(name = "status_transacao")
public class StatusTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
}
