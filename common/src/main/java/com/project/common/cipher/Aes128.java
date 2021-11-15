package com.project.common.cipher;

import com.project.common.model.User;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Aes128 implements SymmetricCipher{

    private Key keySpec;
    private IvParameterSpec iv;
    private String algorithm;

    public Aes128(String key) {
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(UTF_8);
        System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        this.keySpec = keySpec;
        this.iv = new IvParameterSpec(key.substring(0, 16).getBytes());
        this.algorithm = "AES/CBC/PKCS5Padding";
    }


    public String encrypt(String input)
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes(UTF_8));
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String cipherText)
            throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec,iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText,UTF_8);
    }

    public User encryptUser(User user) throws Exception {
        for(Field field : user.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.getType().equals(String.class)){
                if(StringUtils.isEmpty((String)field.get(user))) continue;
                String value=(String)field.get(user);
                try {
                    field.set(user,encrypt(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public User decryptUser(User user) throws Exception {
        for(Field field : user.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.getType().equals(String.class)){
                if(StringUtils.isEmpty((String)field.get(user))) continue;
                String value=(String)field.get(user);
                try {
                    field.set(user,decrypt(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

}
