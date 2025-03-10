package com.cargomaze.cargo_maze.service;

import com.cargomaze.cargo_maze.services.AuthServices;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthServices authServices;

    @BeforeEach
    void setUp() {
        authServices = new AuthServices();
    }

    @Test
    void testGetEmailFromToken_success() throws Exception {
        String token = "valid-token";

        JWTClaimsSet claimsSet = mock(JWTClaimsSet.class);
        when(claimsSet.getStringClaim("email")).thenReturn("test@example.com");

        SignedJWT signedJWT = mock(SignedJWT.class);
        when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);

        try (MockedStatic<JWTParser> mockedJWTParser = mockStatic(JWTParser.class)) {
            mockedJWTParser.when(() -> JWTParser.parse(token)).thenReturn(signedJWT);
            String email = authServices.getEmailFromToken(token);
            assertEquals("test@example.com", email);
        }
    }

    @Test
    void testGetEmailFromToken_tokenInvalid() throws Exception {
        // Simulamos un token inválido
        String token = "invalid-token";

        // Simulamos que el parseo del token lanza una RuntimeException (que no es checked)
        try (MockedStatic<JWTParser> mockedJWTParser = mockStatic(JWTParser.class)) {
            mockedJWTParser.when(() -> JWTParser.parse(token)).thenThrow(new RuntimeException("Invalid token"));

            // Llamamos al servicio
            String email = authServices.getEmailFromToken(token);

            // Verificamos que el valor retornado sea null
            assertNull(email);
        }
    }


    @Test
    void testGetEmailFromToken_noEmailClaim() throws Exception {
        String token = "valid-token-without-email";

        JWTClaimsSet claimsSet = mock(JWTClaimsSet.class);
        when(claimsSet.getStringClaim("email")).thenReturn(null);  // No retorna nada para el "email"

        SignedJWT signedJWT = mock(SignedJWT.class);
        when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);

        try (MockedStatic<JWTParser> mockedJWTParser = mockStatic(JWTParser.class)) {
            mockedJWTParser.when(() -> JWTParser.parse(token)).thenReturn(signedJWT);
            String email = authServices.getEmailFromToken(token);
            assertNull(email);
        }
    }
}
