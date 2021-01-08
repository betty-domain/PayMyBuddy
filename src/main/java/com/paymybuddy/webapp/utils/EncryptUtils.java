package com.paymybuddy.webapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptUtils {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * cryptage d'un mot de passe
     * @param password mot de passse à crypter
     * @return mot de passe crypté
     */
    public String encodePassword(String password)
    {
        return  encoder.encode(password);
    }
}
