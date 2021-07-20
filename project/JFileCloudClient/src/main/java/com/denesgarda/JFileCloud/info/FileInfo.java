package com.denesgarda.JFileCloud.info;

import java.io.Serializable;

public class FileInfo implements Serializable {
    public String path;
    public byte[] data;

    public FileInfo(String path, byte[] data) {
        this.path = path;
        this.data = data;
    }
}
