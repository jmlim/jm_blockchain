package com.jmlim.blockchain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Util 클래스
 * */
public class Util {
    private static Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     *  MessageDigest라는 라이브러리를 이용해서 ‘SHA-256’ 해시 알고리즘을 사용하겠다고 명시.
     *  이후에 사용자로부터 받은 입력을 SHA-256 해시를 적용하여 그 값을 바이트(Byte) 배열 형태로 반환하는 함수.
     */
    public static String getHash(String input) {
        StringBuffer result = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());
            byte bytes[] = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                result.append(
                        Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1)
                );
            }
        } catch (Exception e) {
            logger.error("hash Exception", e);
            e.printStackTrace();
        }
        return result.toString();
    }
}