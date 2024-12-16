package com.biblioteca.security;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

class CustomAuthenticationEntryPointTest {

    /**
     * @throws IOException
     */
    @Test
    void testCommence() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        // Verificar que el código de estado 401 se haya establecido
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Verificar que el mensaje personalizado se haya escrito en la respuesta
        verify(response.getWriter()).write("Error 401: No estás autenticado. Por favor, proporciona credenciales válidas.");
    }
}
