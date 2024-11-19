package com.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblioteca.model.Usuario; // Cambiado de controller a model

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
