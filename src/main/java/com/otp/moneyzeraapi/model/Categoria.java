package com.otp.moneyzeraapi.model;

import com.otp.moneyzeraapi.enums.TipoCategoria;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "categoria")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

}
