package com.otp.moneyzeraapi.dto;

import com.otp.moneyzeraapi.model.Usuario;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
public class UsuarioLoginDto {

    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String senha;

}
