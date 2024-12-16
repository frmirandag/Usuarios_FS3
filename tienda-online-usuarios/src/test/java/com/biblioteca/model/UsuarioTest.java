package com.biblioteca.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        // Crear un validador para ejecutar las validaciones de JSR-303
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan.perez@example.com");
        usuario.setContraseña("password123");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        assertTrue(violations.isEmpty(), "No debe haber violaciones de validación.");
    }

    @Test
    public void testInvalidNombre() {
        Usuario usuario = new Usuario();
        usuario.setNombre(""); // Nombre vacío
        usuario.setCorreo("juan.perez@example.com");
        usuario.setContraseña("password123");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación.");
        assertEquals(1, violations.size(), "Debe haber exactamente una violación de validación.");
        assertEquals("El nombre no puede estar vacío", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidCorreo() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan.perez"); // Correo no válido
        usuario.setContraseña("password123");

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación.");
        assertEquals(1, violations.size(), "Debe haber exactamente una violación de validación.");
        assertEquals("El correo debe ser válido", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidContraseña() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setCorreo("juan.perez@example.com");
        usuario.setContraseña("123"); // Contraseña demasiado corta

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        assertFalse(violations.isEmpty(), "Debe haber violaciones de validación.");
        assertEquals(1, violations.size(), "Debe haber exactamente una violación de validación.");
        assertEquals("La contraseña debe tener al menos 6 caracteres", violations.iterator().next().getMessage());
    }
}
