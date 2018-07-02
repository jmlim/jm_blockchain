package com.jmlim.blockchain.util;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

/**
 * 단순히 특정한 개인키 혹은 공개키 정보를 읽어서 저장한 뒤에 파일로 내보내는 역할만을 수행
 * */
public class Pem {
    private PemObject pemObject;

    public Pem(Key key, String description) {
        this.pemObject = new PemObject(description, key.getEncoded());
    }

    public void write(String filename) throws FileNotFoundException, IOException {
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));

        try {
            pemWriter.writeObject(this.pemObject);
        } finally {
            pemWriter.close();
        }
    }
}
