package com.example.projectbackend.utils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {
    private static final String Key="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMLeGZWQMt8LNEKC" +
            "hgSyFBHk4/PMVczlOTh6ryDICeGm2MfYNrQTNJDYjQ8x+1LbVpwKMgFI/osbKXae" +
            "wFs+PWV9JY+4uhPoRQTDBoiWUxkXumncMDR4Ts7y8cbimMap6k75SWeVu9N7rilA" +
            "nPh7idifSqb0QkdynJDPUtJjdCKlAgMBAAECgYABSx4aWwmrZ1SieGFRWKk/cSh+" +
            "LsYHBSJ7GH1fF1TRdZ0MvoembFY1++n7xtYnN6N8h6jLT2O5IqYEzoryUBhyOAmO" +
            "rJGr3MBreksZXdCptTgf3M314hfWyQwolf7R4dqQ/W4Mtpdt1loswaLIkiU/mAvx" +
            "0b7Xh2qhaQP/Lo31JQJBAOoHfL/Yfpk8nQpbpKqqw5cfdqjhhDztB76SvfQDYJ3D" +
            "Ug1AFkeMpKSwYI59Ib7wfGpy1sq5f7wBt94ciF/cU78CQQDVKWzwcFYW9XAhFQWE" +
            "EDEVmIxA8noO0apvQTyhQCMmXvzsmgF607ZTKK5TDqaFYshRXOTxPLONUMw0D+uH" +
            "VhKbAkEAvba6Gy8eWYx4ZbgqwatSiQSZhYfjUhx4JbYBRHkkbl+y+UjVhsTthw/w" +
            "BjJME+QRh512n7pGYRsneWe56bPLMwJAFFq6TlNLxTF2nZIWTsAFD9zbN7Bvu06G" +
            "Cc2e7n7jXFyhpi4migYBWPt/eeriN8Zw/mPycJ6tPodCmI4UnX24lQJAOHPWeE6F" +
            "l4v1dTGrgI4lYeee1+DRj+HnZ3vzTzUrYqDa6/dnsFaEE9+fgV+2A6bHKkTrX6MS" +
            "aJHwRuNaL4krvQ==";

    private static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        // 假设key是Base64编码的PKCS#8私钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        // 创建PKCS#8编码的KeySpec
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 获取RSA密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 生成PrivateKey
        return keyFactory.generatePrivate(keySpec);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyFromString(Key));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
