package com.jmlim.blockchain.core;

import com.jmlim.blockchain.util.EC;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

@Getter
public class Wallet {
    private static final String ALGORITHM = "SHA1withECDSA";

    private PrivateKey privateKey;
    private PublicKey publicKey;


    //특정한 파일로부터 개인키 및 공개키를 불러와 초기화한다.
    public void setFromFile(String privateKey, String publicKey) throws Exception {
        this.privateKey = new EC().readPrivateKeyFromPemFile(privateKey);
        this.publicKey = new EC().readPublicKeyFromPemFile(publicKey);
    }


    //특정한 데이터를 개인키로 서명해서 얻은 결과를 문자열로 반환한다.
    public String sign(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature;
        signature = Signature.getInstance(ALGORITHM);
        signature.initSign(privateKey);
        byte[] baText = data.getBytes("UTF-8");
        signature.update(baText);
        byte[] baSignature = signature.sign();
        return (new BigInteger(1, baSignature).toString(16).toUpperCase());
    }
}
