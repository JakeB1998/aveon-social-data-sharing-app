package com.example.madcompetition.backend.server.ftp;

import java.io.File;
import java.io.Serializable;

public class FileContent implements Serializable {

    private File file;
    private byte[] payload;

    public FileContent(byte[] content)
    {
        this.setPayload(content);
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
