package com.denesgarda.JFileCloud;

import com.denesgarda.JFileCloud.info.Dir;
import com.denesgarda.JFileCloud.info.FileInfo;
import com.denesgarda.JFileCloud.util.Files;
import com.denesgarda.JarData.data.Serialized;
import com.denesgarda.JarData.data.statics.Serialization;
import com.denesgarda.Prop4j.data.PropertiesFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String ping() {
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            dataOutputStream.writeUTF("PING");
            String message = dataInputStream.readUTF();

            dataOutputStream.close();
            dataInputStream.close();
            socket.close();

            return message;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean fetch(Credentials credentials, String dir) {
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            dataOutputStream.writeUTF("FETCH " + credentials.username() + " " + credentials.password());
            String message = dataInputStream.readUTF();

            boolean returnValue = !message.equals("INVALID");

            Dir info = (Dir) new Serialized(message).deSerialize();
            Files.clearDir(new File(dir));
            for(FileInfo fileInfo : info.files) {
                if(fileInfo.path.substring(2).contains("/")) {
                    String[] folders = fileInfo.path.split("/");
                    String[] newFolders = Arrays.copyOf(folders, folders.length - 1);
                    String path = "";
                    for(String folder : newFolders) {
                        path += folder + "/";
                    }
                    path = dir + File.separator + path.substring(1, path.length() - 1);
                    File make = new File(path);
                    make.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(dir + File.separator + fileInfo.path);
                fileOutputStream.write(fileInfo.data);
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            dataOutputStream.close();
            dataInputStream.close();
            socket.close();

            return returnValue;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean push(Credentials credentials, String dir) {
        try{
            Socket socket = new Socket(address, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            Dir response = Dir.generate(dir);
            dataOutputStream.writeUTF("PUSH " + credentials.username() + " " + credentials.password() + " " + Serialization.serialize(response).getData());

            boolean returnValue = dataInputStream.readUTF().equals("SUCCESS");

            dataOutputStream.close();
            dataInputStream.close();
            socket.close();

            return returnValue;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
