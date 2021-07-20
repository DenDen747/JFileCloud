package com.denesgarda.JFileCloud;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 7900);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        dataOutputStream.writeUTF("Hello there");
        String message = dataInputStream.readUTF();
        System.out.println(message);

        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }
}
