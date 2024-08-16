package cn.karent.util;

import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.crypto.spec.SM2PrivateKeySpec;
import com.tencent.kona.crypto.spec.SM2PublicKeySpec;
import com.tencent.kona.crypto.util.SM2Ciphertext;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.HexFormat;

/**
 * 国密加解密和加验签工具类
 *
 * @author wanshengdao
 * @date 2024/8/15
 */
@Slf4j
public final class Sm2Utils {

    static {
        Security.addProvider(new KonaCryptoProvider());
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    public static class KeyHolder {

        private final String privateKey;

        private final String publicKey;

    }

    /**
     * 生成公私钥 <br/>
     * <b>注: 公钥如果其他算法不兼容是因为这里生成的公钥有04</b>
     *
     * @return 公、私钥对
     */
    public static KeyHolder generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("SM2");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
            String publicKeyHex = HexFormat.of().formatHex(publicKey.getEncoded());
            String privateKeyHex = HexFormat.of().formatHex(privateKey.getEncoded());
            log.debug("hex: {}", publicKeyHex);
            log.debug("pri hex: {}", privateKeyHex);
            return KeyHolder.builder()
                    .privateKey(privateKeyHex)
                    .publicKey(publicKeyHex)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 加密, 输出raw格式
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
            byte[] bytes = cipher.doFinal(data);
            // 已加密数据解成byte并解密
            return SM2Ciphertext.builder()
                    .format(SM2Ciphertext.Format.DER_C1C3C2)
                    .encodedCiphertext(bytes)
                    .build()
                    .rawC1C3C2();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 解密, 输入raw格式
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

            // 转换数据的格式
            data = SM2Ciphertext.builder()
                    .format(SM2Ciphertext.Format.RAW_C1C3C2)
                    .encodedCiphertext(data)
                    .build()
                    .derC1C3C2();

            return cipher.doFinal(data);
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
