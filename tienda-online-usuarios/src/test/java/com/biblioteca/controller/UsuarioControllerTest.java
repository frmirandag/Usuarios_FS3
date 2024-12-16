package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setCorreo("juan@example.com");
        usuario.setContraseña("12345");
    }

    @Test
    public void testListarUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    public void testCrearUsuario() throws Exception {
        String usuarioJson = "{\"nombre\":\"Juan\",\"correo\":\"juan@example.com\",\"contraseña\":\"12345\"}";

        when(usuarioService.guardarUsuario(ArgumentMatchers.any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testObtenerUsuarioExistente() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));
    }

    @Test
    public void testObtenerUsuarioNoExistente() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("El usuario con ID 1 no fue encontrado."));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        String usuarioJson = "{\"nombre\":\"Juan Actualizado\",\"correo\":\"juan_actualizado@example.com\",\"contraseña\":\"67890\"}";
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Juan Actualizado");
        usuarioActualizado.setCorreo("juan_actualizado@example.com");
        usuarioActualizado.setContraseña("67890");

        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.guardarUsuario(ArgumentMatchers.any(Usuario.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    public void testActualizarUsuarioNoExistente() throws Exception {
        String usuarioJson = "{\"nombre\":\"Juan Actualizado\",\"correo\":\"juan_actualizado@example.com\",\"contraseña\":\"67890\"}";

        when(usuarioService.obtenerUsuario(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("El usuario con ID 1 no fue encontrado."));
    }
}
