package com.denesgarda.JFileCloud;

import com.denesgarda.JFileCloud.util.Logger;

import java.io.*;
import java.net.Socket;

public class EchoThread extends Thread {
    private Socket socket;

    public EchoThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String message = dataInputStream.readUTF();
            Server.logger.log(Logger.Level.INFO, "Received message: " + message);
            if(message.equals(Server.config.getProperty("quitMessage"))) {
                Server.logger.log(Logger.Level.INFO, "Quit command received from client.");
                Server.logger.log(Logger.Level.INFO, "Terminating server...");
                System.exit(0);
            }
            //dataOutputStream.writeUTF("Hello!");

            dataInputStream.close();
            dataOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
