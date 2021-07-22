package com.otp.moneyzeraapi.controller;

import com.otp.moneyzeraapi.model.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioController {

    @GetMapping
    public List<Usuario> obter() {
        return  null;
    }
}
