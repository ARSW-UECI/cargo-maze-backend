package com.cargomaze.cargo_maze.services;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    private static final Logger logger = LoggerFactory.getLogger(AuthServices.class);

    public String getEmailFromToken(String token) {
        try {
            SignedJWT signedJWT = (SignedJWT) JWTParser.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim("email");
        } catch (Exception e) {
            logger.error("Failed to parse token: {}", token, e);
            return null;
        }
    }
}
