package com.otp.moneyzeraapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otp.moneyzeraapi.exception.ErroAutenticacao;
import com.otp.moneyzeraapi.exception.RegraNegocioException;
import com.otp.moneyzeraapi.form.UsuarioForm;
import com.otp.moneyzeraapi.form.UsuarioLoginForm;
import com.otp.moneyzeraapi.model.Usuario;
import com.otp.moneyzeraapi.service.interfaces.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
// * Anotação necessária para subir o contexto REST apenas para test
// * onde dá para especificar quais controller estarão de pé
@WebMvcTest(controllers = UsuarioController.class)
// * Serve para ter acesso a um objeto chamado MockMvc
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    static final String API = "/api/usuario";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsuarioService service;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        // * Cenário
        final String email = "irlan.freitas@teste.com";
        final String senha = "1234";

        final UsuarioLoginForm usuario = UsuarioLoginForm.builder()
                .email(email)
                .senha(senha)
                .build();

        final Usuario usuarioAutenticado = Usuario.builder()
                .id(1L)
                .email(email)
                .senha(senha)
                .build();

        Mockito.when(service.autenticar(email, senha)).thenReturn(usuarioAutenticado);
        String json = new ObjectMapper().writeValueAsString(usuario);

        // * Execução
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // * Validação
        mockMvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioAutenticado.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioAutenticado.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioAutenticado.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(usuarioAutenticado.getData()));


    }

    @Test
    public void deveRetornarBadRequestAoAutenticarUmUsuario() throws Exception {
        // * Cenário
        final String email = "irlan.freitas@teste.com";
        final String senha = "1234";

        final UsuarioLoginForm usuario = UsuarioLoginForm.builder()
                .email(email)
                .senha(senha)
                .build();

        Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
        String json = new ObjectMapper().writeValueAsString(usuario);

        // * Execução
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // * Validação
        mockMvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveCadastrarUmUsuario() throws Exception {
        final UsuarioForm usuarioForm = UsuarioForm.builder()
                .nome("Irlan Freitas")
                .email("irlan@contato.com")
                .senha("1234")
                .build();

        final Usuario usuarioCriado = Usuario.builder()
                .id(1L)
                .nome("Irlan Freitas")
                .email("irlan@contato.com")
                .senha("1234")
                .data(LocalDate.now())
                .build();

        Mockito.when(service.salvar(usuarioForm.getUsuario())).thenReturn(usuarioCriado);

        String json = new ObjectMapper().writeValueAsString(usuarioForm);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void deveLancarExcessaoAoCadastrarUmUsuarioInvalido() throws Exception {
        final UsuarioForm usuarioForm = UsuarioForm.builder()
                .nome("Irlan Freitas")
                .email("irlan@contato.com")
                .senha("1234")
                .build();

        Mockito.when(service.salvar(usuarioForm.getUsuario())).thenThrow(RegraNegocioException.class);

        String json = new ObjectMapper().writeValueAsString(usuarioForm);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}