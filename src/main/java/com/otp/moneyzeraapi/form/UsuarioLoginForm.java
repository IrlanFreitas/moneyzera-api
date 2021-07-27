package com.otp.moneyzeraapi.form;

import com.otp.moneyzeraapi.model.Usuario;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
public class UsuarioLoginForm {

    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String senha;

}
