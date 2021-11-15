package com.project.common.cipher;

import com.project.common.model.User;

public interface SymmetricCipher {
    String encrypt(String s) throws Exception;
    String decrypt(String s) throws Exception;
    User encryptUser(User user) throws Exception;
    User decryptUser(User user) throws Exception;
}
