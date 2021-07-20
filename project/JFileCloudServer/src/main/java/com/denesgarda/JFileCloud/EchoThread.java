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
            dataOutputStream.writeUTF("Hello!");

            dataInputStream.close();
            dataOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
