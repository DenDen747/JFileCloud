package com.denesgarda.JFileCloud;

import com.denesgarda.JFileCloud.err.InitializeException;
import com.denesgarda.JFileCloud.util.Logger;
import com.denesgarda.Prop4j.data.PropertiesFile;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket = null;
    public Socket socket = null;

    private Logger logger = new Logger();

    public Server() {

    }

    public void start() {
        logger.log(Logger.Level.INFO, "Starting server...");
        File server = new File(".server");
        if(!server.exists()) {
            logger.log(Logger.Level.WARNING, "Necessary server files not found.");
            logger.log(Logger.Level.INFO, "Creating new files...");
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
                        logger.log(Logger.Level.INFO, "Initialized server files. Change the files to your liking, then restart the server. All files are located in a hidden folder called \".server\"");
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
            logger.log(Logger.Level.INFO, "Server started.");
            while(true) {
                socket = serverSocket.accept();
                logger.log(Logger.Level.INFO, "Client connected");
                new EchoThread(socket).start();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
