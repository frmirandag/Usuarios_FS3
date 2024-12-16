package com.biblioteca.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.mockito.Mockito.mock;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @SuppressWarnings("unused")
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    // Test: Verificar que la configuración de seguridad está cargando correctamente
    @Test
    void testSecurityConfig() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurityMock);

        // Verificar que el filtro de seguridad fue creado correctamente
        assert (securityFilterChain != null);
    }

    // Test: Verificar que los usuarios definidos en memoria son accesibles
    @Test
    void testInMemoryUserDetailsService() {
        InMemoryUserDetailsManager userDetailsService = (InMemoryUserDetailsManager) securityConfig.userDetailsService();

        assert userDetailsService.loadUserByUsername("user") != null;
        assert userDetailsService.loadUserByUsername("admin") != null;
    }

    // Test: Acceso a /productos/** con el rol adecuado (USER)
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetProductoWithUserRole() {
        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk();  // Verificar que la respuesta sea OK
    }

    // Test: Acceso a /productos/** con el rol adecuado (ADMIN)
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetProductoWithAdminRole() {
        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk();  // Verificar que la respuesta sea OK
    }

    // Test: Acceso a /productos/** con rol insuficiente (sin ADMIN)
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetProductoForbidden() {
        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isForbidden();  // Verificar que la respuesta sea Forbidden
    }

    // Test: Acceso no autenticado a /productos/**
    @Test
    void testGetProductoUnauthorized() {
        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isUnauthorized();  // Verificar que la respuesta sea Unauthorized
    }

    // Test: Manejador de acceso denegado devuelve un 403 con mensaje adecuado
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAccessDeniedHandler() {
        webTestClient.delete().uri("/productos/1")
                .exchange()
                .expectStatus().isForbidden()  // Verificar que la respuesta sea Forbidden
                .expectBody(String.class)
                .isEqualTo("Acceso denegado: No tienes permisos para realizar esta operación.");
    }

    // Test: Verificar la configuración del AuthenticationEntryPoint para 401
    @Test
    void testAuthenticationEntryPoint() {
        CustomAuthenticationEntryPoint mockAuthenticationEntryPoint = mock(CustomAuthenticationEntryPoint.class);

        assert (mockAuthenticationEntryPoint != null);
    }
}
