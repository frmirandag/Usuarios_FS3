package com.biblioteca.repository;

import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest // Anotación para pruebas de JPA
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Crear un usuario de prueba
        usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan.perez@correo.com");
        usuario.setContraseña("password123");
    }

    @Test
    public void testGuardarUsuario() {
        // Guardar el usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Verificar que el usuario se haya guardado correctamente
        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getId()).isGreaterThan(0);
    }

    @Test
    public void testBuscarUsuarioPorCorreo() {
        // Guardar el usuario
        usuarioRepository.save(usuario);

        // Buscar el usuario por su correo
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuario.getId());

        // Verificar que el usuario ha sido encontrado correctamente
        assertThat(usuarioEncontrado).isNotNull();
        assertThat(usuarioEncontrado.get()).isEqualTo(usuario.getId());
    }
}
