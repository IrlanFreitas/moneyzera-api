package com.otp.moneyzeraapi.model;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private LocalDate data = LocalDate.now();
}
