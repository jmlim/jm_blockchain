package com.jmlim.blockchain.core;

import com.jmlim.blockchain.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.Signature;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@Slf4j
public class Block {

    private static final String ALGORITHM = "SHA1withECDSA";

    private int blockID;
    private String previousBlockHash;
    private int nonce;
    private ArrayList<Transaction> transactionList;

    //public void addTransaction(Transaction transaction) {
    //    transactionList.add(transaction);
    //}

    //특정한 트랜잭션이 정상적인지 검증
    private boolean verifyTransaction(Transaction transaction) throws Exception {
        Signature signature;
        signature = Signature.getInstance(ALGORITHM);
        byte[] baText = transaction.getData().getBytes("UTF-8");
        signature.initVerify(transaction.getSender());
        signature.update(baText);
        return signature.verify(new BigInteger(transaction.getSignature(), 16).toByteArray());
    }

    //정상적인 트랜잭션에 한해서 블록에 추가한다.
    public void addTransaction(Transaction transaction) throws Exception {
        if (verifyTransaction(transaction)) {
            log.info("정상적인 트랜잭션을 발견했습니다.");
            transactionList.add(transaction);
        } else {
            log.info("트랜잭션이 바르게 인증되지 않았습니다.");
        }
    }


    public void showInformation() {
        log.info("--------------------------------------");
        log.info("블록 번호: {}", blockID);
        log.info("이전 해시: {}", previousBlockHash);
        log.info("채굴 변수 값: {}", nonce);
        log.info("트랜잭션 개수: {} 개", transactionList.size());

        for (Transaction transaction : transactionList) {
            log.info(transaction.getInformation());
        }
        log.info("블록 해시: {}", getBlockHash());
        log.info("--------------------------------------");
    }

    public String getBlockHash() {
        /** 거래정보를 더하여 스트링으로 만들어서 같이 더해서 해쉬를 뽑음*/
        String transactionInformations = "";
        for (Transaction transaction : transactionList) {
            transactionInformations += transaction.getInformation();
        }

        return Util.getHash(nonce + transactionInformations + previousBlockHash);
    }

    /**
     * 위 소스코드는 임의의 입력 값에 대해서 SHA-256을 적용한 결과 값이 앞에서부터
     * 4자리만큼 모두 문자 '0' 으로 구성되어있는지 물어보고, 만약 4자리가 모두 '0'으로 구성되어 있다면 정답을 찾았다고 출력하는 소스코드다.
     * <p>
     * 실제 대부분의 작업 증명 합의 알고리즘을 사용하는 블록체인 시스템은 위와 같이 무작위의 입력 갑을 대입하여 정답(Nonce) 과정으로 채굴(Mining)을 진행하도록 설계되어 있다
     */
    public void mine() {
        while (true) {
            if (getBlockHash().substring(0, 4).equals("0000")) {
                log.info("{} 번째 블록의 채굴에 성공했습니다.", blockID);
                break;
            }
            nonce++;
        }
    }
}

