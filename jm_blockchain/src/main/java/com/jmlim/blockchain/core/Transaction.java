package com.jmlim.blockchain.core;

import com.jmlim.blockchain.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
public class Transaction {
    private String signature;
    private PublicKey sender;
    private PublicKey receiver;
    double amount;
    Timestamp timestamp;

    //서명값을 포함한 트랜잭션 정보 출력
    public String getInformation() {
        return "<" + signature + ">\n" +
                new Util().getHash(sender.toString()) + " -> " +
                new Util().getHash(receiver.toString()) + " : " +
                amount + "개 (" + timestamp + ")";
    }

    // 서명 값을 제외한 단순 트랜잭션 정보를 출력
    public String getData() {
        return new Util().getHash(sender.toString()) + " -> " +
                new Util().getHash(receiver.toString()) + " : " +
                amount + "개 (" + timestamp + ")";
    }

    // 어떠한 지갑 주소로 얼마만큼의 가상화폐를 보냈는지에 대한 정보를 기준으로 초기화
    public Transaction(Wallet wallet, PublicKey receiver, double amount, String timestamp) throws Exception {
        this.sender = wallet.getPublicKey();
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = Timestamp.valueOf(timestamp);
        this.signature = wallet.sign(getData());
    }
}
