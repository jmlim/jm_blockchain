package com.jmlim.blockchain.backup;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String sender;
    private String receiver;
    private double amount;

    public String getInformation() {
        return sender + "이(가) " + receiver + "에게 " + amount + "개의 코인을 보냈습니다.";
    }
}