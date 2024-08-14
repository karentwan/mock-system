package cn.karent;

import com.tencent.kona.crypto.KonaCryptoProvider;
import com.tencent.kona.crypto.spec.SM2PrivateKeySpec;
import com.tencent.kona.crypto.spec.SM2PublicKeySpec;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;

/**
 * @author wanshengdao
 * @date 2024/8/14
 */
public class Sm2Test {

    static {
        Security.addProvider(new KonaCryptoProvider());
    }


    @Test
    void testSm2() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] encodedPrivateKey = HexFormat.of().parseHex("00cec6f262743b45cbe209fb3f08fb22f0bbfd1797799506b80ffac10c884f9c5a");

        KeyFactory keyFactory = KeyFactory.getInstance("SM2");

        SM2PrivateKeySpec privateKeySpec = new SM2PrivateKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        Signature signature = Signature.getInstance("SM2");
        signature.initSign(privateKey);

        String str = "POST\n" +
                "/g2/v0/payment/acq/90751888/evo.offline.payment\n" +
                "20240814141759+08:00\n" +
                "689a0b8325734cfda3106ff69000d924\n" +
                "{\"payment\":{\"evoTransInfo\":{\"evoTransID\":\"c925da665ecf462b9232c8183273705f\",\"evoTransTime\":\"2024-08-13T09:14:39Z\",\"retrievalReferenceNum\":\"422617451069\",\"traceNum\":\"451069\"},\"merchantTransInfo\":{\"merchantOrderReference\":\"P31240857693\",\"merchantTransID\":\"411122312845531240857693\",\"merchantTransTime\":\"2024-08-07T09:43:35+08:00\"},\"pspTransInfo\":{\"authorizationCode\":\"059760\",\"pspTransID\":\"304226332799292\",\"pspTransTime\":\"2024-08-13T09:14:37Z\"},\"status\":\"Captured\",\"transAmount\":{\"currency\":\"CNY\",\"value\":\"1.00\"}},\"paymentMethod\":{\"card\":{\"first6No\":\"476173\",\"fundingType\":\"credit\",\"icCardData\":\"9f360200029108ece5922e00860000\",\"issuingCountry\":\"840\",\"last4No\":\"0453\",\"paymentBrand\":\"VISA\"},\"type\":\"card\"},\"pspData\":{\"name\":\"Visa\"},\"result\":{\"code\":\"S0000\",\"message\":\"Success\",\"pspMessage\":\"Successful approval/completion or V .I.P .PIN\\n verification is successful\",\"pspResponseCode\":\"00\"}}";
        byte[] sign = signature.sign();
        String result = HexFormat.of().formatHex(sign);
        System.out.println(result);


    }
}
