package com.denesgarda.JFileCloud;

import com.denesgarda.JFileCloud.err.InitializeException;
import com.denesgarda.Prop4j.data.PropertiesFile;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket = null;
    public Socket socket = null;

    public Server() {

    }

    public void start() {
        File server = new File(".server");
        if(!server.exists()) {
            boolean mkdir1 = server.mkdirs();
            if(!mkdir1) {
                throw new InitializeException();
            }
            File propertiesFile = new File(".server/config.properties");
            if(!propertiesFile.exists()) {
                try {
                    boolean newFile1 = propertiesFile.createNewFile();
                    if(!newFile1) {
                        throw new InitializeException();
                    }
                    PropertiesFile config = new PropertiesFile(".server/config.properties");
                    config.setProperty("port", "7900").setProperty("maxSpace", "10737418240");
                    File profiles = new File(".server/profiles");
                    boolean mkdir2 = profiles.mkdirs();
                    if(!mkdir2) {
                        throw new InitializeException();
                    }
                    File adminFile = new File(".server/profiles/admin.properties");
                    try {
                        boolean newFile2 = adminFile.createNewFile();
                        if(!newFile2) {
                            throw new InitializeException();
                        }
                        PropertiesFile admin = new PropertiesFile(".server/profiles/admin.properties");
                        admin.setProperty("password", "admin");
                        System.out.println("Initialized server files. Change the files to your liking, then restart the server. All files are located in a hidden folder called \".server\"");
                        System.exit(0);
                    }
                    catch(Exception e) {
                        throw new InitializeException();
                    }
                }
                catch(Exception e) {
                    throw new InitializeException();
                }
            }
        }
        PropertiesFile config = new PropertiesFile(".server/config.properties");
        try {
            serverSocket = new ServerSocket(Integer.parseInt(config.getProperty("port")));
            System.out.println("Server started.");
            while(true) {
                socket = serverSocket.accept();
                new EchoThread(socket).start();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
