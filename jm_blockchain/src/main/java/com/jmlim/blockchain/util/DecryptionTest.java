package com.jmlim.blockchain.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Slf4j
public class DecryptionTest {
    public static void main(String[] args) throws Exception {
        //무작위의 개인키와 공개키를 생성하기 위한 키 생성 객체 정의
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");

        // 타원 곡선 디지털 서명 알고리즘 객체 생성.
        ECGenParameterSpec ecsp;
        //세부 알고리즘 스펙을 정의
        ecsp = new ECGenParameterSpec("sect163k1");
        //랜덤으로 임의의 키를 생성.
        kpg.initialize(ecsp, new SecureRandom());

        //개인키와 공개키 한 쌍을 생성한다.
        KeyPair kp = kpg.genKeyPair();
        PrivateKey privKey = kp.getPrivate();
        PublicKey pubKey = kp.getPublic();

        //서명 객체를 생성해 개인키를 설정
        Signature ecdsa;
        ecdsa = Signature.getInstance("SHA1withECDSA", "SunEC");
        ecdsa.initSign(privKey);

        //임의의 원래 문장을 정의
        String text = "정묵이가 현철이한테 1000 코인 전송";
        //변경된문장
        String textInfected = "정묵이가 현철이한테 3000 코인 전송";
        log.info("원래 문장: {} ", text);
        log.info("변경된 문장: {} ", textInfected);

        //원래 문장에 대해 암호화를 수행해 서명 값(암호문)을 얻음.
        ecdsa.update(text.getBytes("UTF-8"));
        byte[] signatureByte = ecdsa.sign();
        log.info("암호문: 0x {}", (new BigInteger(1, signatureByte).toString(16)).toUpperCase());

        //서명 객체를 생성해 공개키를 이용해 복호화 할 수 있도록 설정함
        Signature signature;
        signature = Signature.getInstance("SHA1withECDSA", "SunEC");
        signature.initVerify(pubKey);

        //원래 문장을 공개키로 복호화해 검증한다.
        signature.update(text.getBytes("UTF-8"));
        log.info("원래 문장 검증: {} ", signature.verify(signatureByte));

        /**
         * 소스코드를 실행해보면 위와 같이 특정한 개인키로 서명한 문장은 반드시 그 개인키와 한 쌍이 되는 공개키로 검증을 했을 때 참(True) 값이 나오는 것을 알 수 있다.
         * 즉 특정한 암호문을 작성한 사람이 ‘누구’인지를 검증할 수 있는 것이다.
         * 해당 공개키와 한 쌍이 되는 개인키를 모른다면 해당 암호문을 작성할 수 없으므로 누가 해당 암호문을 작성했는지 정확히 알아낼 수 있다.
         * 이제 다음과 같이 ‘변경된 문장’을 만들어 추가적으로 복호화를 수행하도록 소스코드를 수정해보자.
         * */
        //변경된 문장 공개키로 검증
        signature.update(textInfected.getBytes("UTF-8"));
        log.info("변경괸 문장 검증: {} ", signature.verify(signatureByte));


    }
}
