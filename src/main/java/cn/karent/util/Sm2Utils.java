package cn.karent.util;

import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.crypto.spec.SM2PrivateKeySpec;
import com.tencent.kona.crypto.spec.SM2PublicKeySpec;
import com.tencent.kona.crypto.util.SM2Ciphertext;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;

/**
 * 国密加解密和加验签工具类
 *
 * @author wanshengdao
 * @date 2024/8/15
 */
public final class Sm2Utils {

    static {
        Security.addProvider(new KonaCryptoProvider());
    }

    /**
     * 加密
     *
     * @param publicKeyHexStr 十六进制公钥
     * @param data            数据
     * @return 加密后的数据
     */
    public static byte[] encrypt(String publicKeyHexStr, byte[] data) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("SM2");
            SM2PublicKeySpec publicKeySpec = new SM2PublicKeySpec(HexFormat.of().parseHex(publicKeyHexStr));
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance("SM2");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 已加密数据解成byte并解密
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 解密
     *
     * @param privateKeyHexStr 私钥
     * @param data             密文
     * @return 明文
     */
    public static byte[] decrypt(String privateKeyHexStr, byte[] data) {
        try {
            // 获取密钥工厂，获取私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance("SM2");
            SM2PrivateKeySpec privateKeySpec = new SM2PrivateKeySpec(HexFormat.of().parseHex(privateKeyHexStr));
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance("SM2");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 已加密数据解成byte并解密
            data = SM2Ciphertext.builder()
                    .format(SM2Ciphertext.Format.RAW_C1C3C2)
                    .encodedCiphertext(data)
                    .build()
                    .derC1C3C2();

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 加签
     *
     * @param privateKeyHexStr 私钥
     * @param data             数据
     * @return 签名
     */
    public static byte[] sign(String privateKeyHexStr, byte[] data) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("SM2");
            SM2PrivateKeySpec privateKeySpec = new SM2PrivateKeySpec(HexFormat.of().parseHex(privateKeyHexStr));
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            Signature signature = Signature.getInstance("SM2");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 验签
     *
     * @param publicKeyHexStr 公钥
     * @param data            数据
     * @param sign            签名
     * @return 验签结果
     */
    public static boolean verify(String publicKeyHexStr, byte[] data, byte[] sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("SM2");
            SM2PublicKeySpec publicKeySpec = new SM2PublicKeySpec(HexFormat.of().parseHex(publicKeyHexStr));
            PublicKey privateKey = keyFactory.generatePublic(publicKeySpec);
            Signature signature = Signature.getInstance("SM2");
            signature.initVerify(privateKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            throw new IllegalStateException(e);
        }
    }

}
