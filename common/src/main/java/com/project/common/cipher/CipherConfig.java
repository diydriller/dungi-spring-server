package com.project.common.cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CipherConfig {

    @Value("${aes.secret}")
    private String aesSecret;

    @Bean
    public SymmetricCipher symmetricCipher() { return new Aes128(aesSecret);}

    @Bean
    public PasswordEncoder hashCipher(){ return new BCryptPasswordEncoder();}

}
