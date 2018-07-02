package com.jmlim.blockchain.backup;


import java.util.ArrayList;


public class BlockChainStarter {
    public static void main(String[] args) {
        Block block1 = new Block(1, null, 0, new ArrayList<Transaction>());
        block1.mine();
        block1.showInformation();

        /**
         * 실제로 비트코인(Bit Coin)을 포함해 대부분의 블록체인에서 하나의 트랜잭션에서 제일 중요한 내용은 누가 누구에게 얼마의 코인을 보냈는지에 대한 정보다.
         * */
        Block block2 = new Block(2, block1.getBlockHash(), 0, new ArrayList<Transaction>());
        block2.addTransaction(new Transaction("나동빈", "박한울", 1.5));
        block2.addTransaction(new Transaction("이태일", "박한울", 0.7));
        block2.mine();
        block2.showInformation();

        Block block3 = new Block(3, block2.getBlockHash(), 0, new ArrayList<Transaction>());
        block3.addTransaction(new Transaction("강종구", "이상욱", 8.2));
        block3.addTransaction(new Transaction("박한울", "나동빈", 0.4));
        block3.addTransaction(new Transaction("임정묵", "신현철", 10.1));
        block3.mine();
        block3.showInformation();

        Block block4 = new Block(4, block3.getBlockHash(), 0, new ArrayList<Transaction>());
        block4.addTransaction(new Transaction("이상욱", "강종구", 0.1));
        block4.mine();
        block4.showInformation();

        /*
        * Transaction transaction = new Transaction("나동빈", "박한울", 1.5);

System.out.println(transaction.getInformation());
        * */
    }
}
