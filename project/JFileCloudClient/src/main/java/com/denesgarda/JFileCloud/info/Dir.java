package com.denesgarda.JFileCloud.info;

import com.denesgarda.JFileCloud.util.ArrayUtils;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Dir implements Serializable {
    public FileInfo[] files;

    public Dir(FileInfo[] files) {
        this.files = files;
    }

    public static Dir generate(String dir) {
        File mainDir = new File(dir);
        return new Dir(list(mainDir, mainDir.getPath()));
    }
    private static FileInfo[] list(File dir, String mainDir) {
        FileInfo[] info = new FileInfo[]{};
        if(dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    for (FileInfo fileInfo : list(file, mainDir)) {
                        info = ArrayUtils.append(info, fileInfo);
                    }
                } else {
                    try {
                        info = ArrayUtils.append(info, new FileInfo(file.getPath().substring(mainDir.length()), Files.readAllBytes(Paths.get(file.getPath()))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return info;
    }
}
