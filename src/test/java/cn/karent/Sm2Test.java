package cn.karent;

import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.crypto.spec.SM2PrivateKeySpec;
import com.tencent.kona.crypto.spec.SM2PublicKeySpec;
import com.tencent.kona.crypto.util.SM2Ciphertext;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.codec.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HexFormat;

/**
 * @author wanshengdao
 * @date 2024/8/14
 */
public class Sm2Test {

    static {
        Security.addProvider(new KonaCryptoProvider());
    }

    /**
     * 生成SM2密钥对并放入自定义封装中
     */
    public static void generateSm2KeyPair() {
        // 在使用KonaCrypto中的任何特性之前，必须要加载KonaCryptoProvider
        Security.addProvider(new KonaCryptoProvider());
        KeyPairGenerator keyPairGenerator = null;
        try {
            // 标准JCA写法
            keyPairGenerator = KeyPairGenerator.getInstance("SM2");
        } catch (NoSuchAlgorithmException e) {
            // 转换为运行时异常
            throw new RuntimeException(e);
        }
        // 拿到密钥对象
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        // 将公钥和私钥转换成16进制字符串，方便传输存储
        // 公钥为长度为65字节，格式为04||x||y，其中04表示非压缩格式，x和y分别为该公钥点在椭圆曲线上的仿射横坐标和纵坐标的值
        String publickeyHex = HexFormat.of().formatHex(publicKey.getEncoded());
        // 私钥长度为32字节
        String privateHex = HexFormat.of().formatHex(privateKey.getEncoded());
        System.out.println("公钥");
        System.out.println(publickeyHex);
        System.out.println("私钥");
        System.out.println(privateHex);
    }

    @Test
    void testGen() {
        generateSm2KeyPair();
    }

    @Test
    void testEncrypt() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        String publicKeyStr = "04a9206146e7e0a666bd6b48526d270f31660eb3499ec9736a0e703fea7198afe119d15b8ace0fae9f11daded0ed4963e65bdfc29aa9b88d757b5a5f5cc9d2e948";
        String data = "hello";

        // 获取密钥工厂，获取私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("SM2");
        SM2PublicKeySpec publicKeySpec = new SM2PublicKeySpec(HexFormat.of().parseHex(publicKeyStr));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        // 初始化cipher
        Cipher cipher = Cipher.getInstance("SM2");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 已加密数据解成byte并解密
        String result = HexFormat.of().formatHex(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        System.out.println(result);
    }


    @Test
    void testDecrypt() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchProviderException {

        String privateKeyStr = "203E9A8C0C0DAD528917D6E2261A3A622630898F698DC7901C16B2F8B84252C1";
        String data = "047cbf589432000ca59d5dd6274f99b4d9d18b1b8795ab21f0d9a8e8e446cad11f81f74673f15c7a9f62985daa4883fbde60dc84b2b22a8feb3c43fc2a839f637dfb624dda0c53e1255e3a6d376b2c52175ab6f7dcb300e4a8fe829ec2abda837132efff27dd";

        byte[] dataByte = HexFormat.of().parseHex(data);
//        String base = "BOcdW6sIM0G4X62d05VNIBST4p4KpNpEYS+fad2sFfLcndIDDzRUuCruiqxs4Pu0mk6uIt/s8XFnEteJMuYJd0wubIGE1WHaivI9P+RqQ8O1i1Jw9+MmSM8FRhSWh7qCE+JBFdF4";
//        byte[] dataByte = Base64.getDecoder().decode(base.getBytes(StandardCharsets.UTF_8));
//        System.out.println(HexFormat.of().formatHex(dataByte));

        // 获取密钥工厂，获取私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("SM2");
        SM2PrivateKeySpec privateKeySpec = new SM2PrivateKeySpec(HexFormat.of().parseHex(privateKeyStr));
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        // 初始化cipher
        Cipher cipher = Cipher.getInstance("SM2");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 已加密数据解成byte并解密
//        byte[] dataByte = HexFormat.of().parseHex(data);
        dataByte = SM2Ciphertext.builder()
                .format(SM2Ciphertext.Format.RAW_C1C3C2)
                .encodedCiphertext(dataByte)
                .build()
                .derC1C3C2();

        byte[] cleartext = cipher.doFinal(dataByte);
        String result = new String(cleartext, StandardCharsets.UTF_8);
        System.out.println(result);
    }

    @Test
    void testSwitch() throws IOException {
//        String data = "306e022004f54d2835e44ef2cdeb1cca3d7313307ebbddc9283b6b5cdaa50c961c08d44a022100889807b051c1406c2030f867b3be1111115fc77faac36bdce9511a2c0969f8bd0420a594715469e2addd40ee48dfb48b227ea4d1008432aa007c95ce5636830df8fc0405f55e773418";
//        byte[] bytes = HexFormat.of().parseHex(data);
//        byte[] result = SM2Ciphertext.builder()
//                .format(SM2Ciphertext.Format.RAW_C1C2C3)
//                .encodedCiphertext(bytes)
//                .build()
//                .derC1C2C3();
//        System.out.println(HexFormat.of().formatHex(result));
    }
}
