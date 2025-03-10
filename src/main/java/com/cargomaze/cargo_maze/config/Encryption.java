package com.cargomaze.cargo_maze.config;
import com.cargomaze.cargo_maze.services.exceptions.EncryptionException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encryption {

    @Value("${my.config.algorithm}")
    private String ALGORITHM;

    @Value("${my.config.secretkey}")
    private String SECRET_KEY;

    private SecretKeySpec generateKey(String key) throws EncryptionException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedKey = digest.digest(key.getBytes());
            byte[] finalKey = new byte[16];
            System.arraycopy(hashedKey, 0, finalKey, 0, 16);
            return new SecretKeySpec(finalKey, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Error al generar la clave: SHA-256 no disponible en este entorno.");
        }
    }

    public String encrypt(String data) throws EncryptionException {
        try {
            SecretKeySpec key = generateKey(SECRET_KEY);

            byte[] ivBytes = new byte[16];
            new java.security.SecureRandom().nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            byte[] encryptedWithIv = new byte[ivBytes.length + encryptedData.length];
            System.arraycopy(ivBytes, 0, encryptedWithIv, 0, ivBytes.length);
            System.arraycopy(encryptedData, 0, encryptedWithIv, ivBytes.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            throw new EncryptionException("Error al generar la clave: SHA-256 no disponible en este entorno.");
        }
    }
}