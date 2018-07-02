package com.jmlim.blockchain.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class EC {
    //세부 알고리즘으로 sect163k1 을 사용.
    private final String ALGORITHM = "sect163k1";

    public void generate(String privateKeyName, String publicKeyName) throws Exception {
        // 바운시 캐슬의 타원 곡석 표준 알고리즘(ECDSA)을 사용
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", "BC");

        // 타원 곡석의 세부 알고리즘으로 sect163k1을 사용한다.
        ECGenParameterSpec ecsp;
        ecsp = new ECGenParameterSpec(ALGORITHM);
        generator.initialize(ecsp, new SecureRandom());

        //해당 알고리즘으로 랜덤의 키 한쌍을 생성한다.
        KeyPair keyPair = generator.generateKeyPair();
        log.info("타원곡선 암호키 한 쌍을 생성했습니다.");

        // 생성한 키 한 쌍에서 개인키와 공개키를 추출한다.
        PrivateKey priv = keyPair.getPrivate();
        PublicKey pub = keyPair.getPublic();

        //개인키와 공개키를 특정한 파일 이름으로 저장한다.
        writePemFile(priv, "EC PRIVATE KEY", privateKeyName);
        writePemFile(pub, "EC PUBLIC KEY", publicKeyName);
    }

    // Pem 클래스를 이용해 생성된 암호키를 파일로 저장하는 메소드
    private void writePemFile(Key key, String description, String filename) throws FileNotFoundException, IOException {
        Pem pemfile = new Pem(key, description);
        pemfile.write(filename);
        log.info("EC 암호키 {}을(를) {} 파일로 내보냈습니다.", description, filename);
    }


    // 문자열 형태의 인증서에서 개인키를 추출하는 함수.

    public PrivateKey readPrivateKeyFromPemFile(String privateKeyName)
            throws FileNotFoundException, IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        String data = readString(privateKeyName);
        log.info("EC 개인키를 {}로 부터 불러왔습니다.", privateKeyName);
        log.info(data);
        // 불필요한 설명 구문을 제거합니다.
        data = data.replaceAll("-----BEGIN EC PRIVATE KEY-----", "");
        data = data.replaceAll("-----END EC PRIVATE KEY-----", "");

        // PEM 파일은 Base64로 인코딩 되어있으므로 디코딩해서 읽을 수 있도록 합니다.
        byte[] decoded = Base64.decode(data);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("ECDSA");
        PrivateKey privateKey = factory.generatePrivate(spec);
        return privateKey;
    }

    // 문자열 형태의 인증서에서 공개키를 추출하는 함수입니다.

    public PublicKey readPublicKeyFromPemFile(String publicKeyName)
            throws FileNotFoundException, IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        String data = readString(publicKeyName);
        log.info("EC 개인키를 {}로 부터 불러왔습니다.", publicKeyName);
        log.info(data);
        // 불필요한 설명 구문을 제거합니다.
        data = data.replaceAll("-----BEGIN EC PUBLIC KEY-----", "");
        data = data.replaceAll("-----END EC PUBLIC KEY-----", "");

        // PEM 파일은 Base64로 인코딩 되어있으므로 디코딩해서 읽을 수 있도록 합니다.
        byte[] decoded = Base64.decode(data);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("ECDSA");
        PublicKey publicKey = factory.generatePublic(spec);
        return publicKey;
    }

    // 특정한 파일에 작성되어 있는 문자열을 그대로 읽어오는 함수.
    private String readString(String filename) throws FileNotFoundException, IOException {
        String pem = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null)
            pem += line + "\n";
        br.close();
        return pem;

    }


   /* public static void main(String[] args) throws Exception {
        //바운시 캐슬의 암호화 라이브러리를 사용하도록 설정합니다.
        Security.addProvider(new BouncyCastleProvider());

        //타원 곡선 객체를 생성해 개인키와 공개키를 각각 private.pem, public.pem 으로 저장합니다.
        EC ec = new EC();
        ec.generate("private.pem", "public.pem");

        //파일로 저장한 개인키와 공개키를 다시 프로그램으로 불러온다.
        PrivateKey privateKey =  ec.readPrivateKeyFromPemFile("private.pem");
        PublicKey publicKey = ec.readPublicKeyFromPemFile("public.pem");

        *//**
         *   프로그램 실행 결과는 다음과 같으며 파일에서 불러온 키 데이터가 그대로 출력되는 것을 확인할 수 있다.
         *   물론 실제 지갑 소프트웨어에서는 이렇게 키 데이터가 노출되어서는 안 된다.
         *   개인키는 비밀번호(Password)와 같은 기능을 수행하므로 외부인이 알지 못하도록 보호해야 한다.
         * *//*
    }*/
    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        EC ec = new EC();
        // 총 두 쌍의 키를 생성해 파일 형태로 저장.
        ec.generate("private1.pem", "public1.pem");
        ec.generate("private2.pem", "public2.pem");

        // 파일 형태로 저장한 키 데이터를 프로그램으로 불러옴.
        PrivateKey privateKey1 = ec.readPrivateKeyFromPemFile("private1.pem");
        PublicKey publicKey1 = ec.readPublicKeyFromPemFile("public1.pem");
        PrivateKey privateKey2 = ec.readPrivateKeyFromPemFile("private2.pem");
        PublicKey publicKey2 = ec.readPublicKeyFromPemFile("public2.pem");


        Signature ecdsa;
        ecdsa = Signature.getInstance("SHA1withECDSA");
        // 개인키 1을 이용해 암호화(서명).
        ecdsa.initSign(privateKey1);
        String text = "평문입니다.";
        log.info("평문 정보: {}", text);
        byte[] baText = text.getBytes("UTF-8");

        // 평문 데이터를 암호화하여 서명한 데이터를 출력.
        ecdsa.update(baText);
        byte[] baSignature = ecdsa.sign();
        System.out.println("서명된 값: 0x" + (new BigInteger(1, baSignature).toString(16)).toUpperCase());

        Signature signature;
        signature = Signature.getInstance("SHA1withECDSA");
        // 검증할 때는 공개키 2를 이용해 복호화를 수행합니다.
        signature.initVerify(publicKey1);
        signature.update(baText);
        boolean result = signature.verify(baSignature);
        // 개인키와 매칭되는 공개키가 아니므로 복호화에 실패.
        // 공개키 1을 이용해 복호화 수행 시 정상적으로 검증처리가 될 것임.
        log.info("신원 검증: {}" , result);
    }
}
