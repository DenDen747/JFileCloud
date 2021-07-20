package com.denesgarda.JFileCloud.util;

import java.io.File;

public class Files {
    public static void clearDir(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteDir(f);
                } else {
                    f.delete();
                }
            }
        }
    }
    public static void deleteDir(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteDir(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
