package com.denesgarda.JFileCloud;

import com.denesgarda.JFileCloud.info.Dir;
import com.denesgarda.JFileCloud.info.FileInfo;
import com.denesgarda.JFileCloud.util.Files;
import com.denesgarda.JFileCloud.util.Logger;
import com.denesgarda.JarData.data.Serialized;
import com.denesgarda.JarData.data.statics.Serialization;
import com.denesgarda.Prop4j.data.PropertiesFile;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Arrays;

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
            String[] args = message.split(" ");
            if(args[0].equals(Server.config.getProperty("quitMessage")) && args.length == 1) {
                Server.logger.log(Logger.Level.REQUEST, message);
                Server.logger.log(Logger.Level.INFO, "Quit command received from client.");
                Server.logger.log(Logger.Level.INFO, "Terminating server...");
                System.exit(0);
            }
            else if(args[0].equals("PING") && args.length == 1) {
                Server.logger.log(Logger.Level.REQUEST, message);
                Server.logger.log(Logger.Level.INFO, "Client pinged server. Replying...");
                String response = "PONG";
                dataOutputStream.writeUTF(response);
                Server.logger.log(Logger.Level.RESPONSE, response);
            }
            else if(args[0].equals("FETCH") && args.length == 3) {
                if(Boolean.parseBoolean(Server.config.getProperty("redactions"))) {
                    Server.logger.log(Logger.Level.REDACTED_REQUEST, "Client " + args[1] + " requested fetch.");
                }
                else {
                    Server.logger.log(Logger.Level.REQUEST, message);
                }
                Server.logger.log(Logger.Level.INFO, "Client requested fetch. Verifying credentials...");
                File profiles = new File(".server/profiles");
                for(File file : profiles.listFiles()) {
                    String name = file.getName().split("\\.")[0];
                    if(name.equals(args[1])) {
                        PropertiesFile profile = new PropertiesFile(".server/profiles/" + name + ".properties");
                        if(args[2].equals(profile.getProperty("password"))) {
                            Server.logger.log(Logger.Level.INFO, "Credentials verified.");
                            Server.logger.log(Logger.Level.INFO, "Sending files...");

                            //String response = Arrays.toString(new String[]{Arrays.toString(new String[]{"test.txt", Arrays.toString(Files.readAllBytes(Paths.get(".server/files/" + name + "/test.txt")))})});
                            Dir response = Dir.generate(name); //new Dir(new FileInfo[]{new FileInfo("test.txt", Files.readAllBytes(Paths.get(".server/files/" + name + "/test.txt")))});
                            dataOutputStream.writeUTF(Serialization.serialize(response).getData());

                            if(!Boolean.parseBoolean(Server.config.getProperty("redactions"))) {
                                Server.logger.log(Logger.Level.RESPONSE, String.valueOf(response));
                            }
                            else {
                                Server.logger.log(Logger.Level.REDACTED_RESPONSE, "Sent files.");
                            }
                        }
                        else {
                            Server.logger.log(Logger.Level.INFO, "Client requested fetch using invalid credentials.");
                            String response = "INVALID";
                            dataOutputStream.writeUTF(response);
                            Server.logger.log(Logger.Level.RESPONSE, response);
                        }
                    }
                    else {
                        Server.logger.log(Logger.Level.INFO, "Client requested fetch using invalid credentials.");
                        String response = "INVALID";
                        dataOutputStream.writeUTF(response);
                        Server.logger.log(Logger.Level.RESPONSE, response);
                    }
                }
            }
            else if(args[0].equals("PUSH") && args.length == 4) {
                if(Boolean.parseBoolean(Server.config.getProperty("redactions"))) {
                    Server.logger.log(Logger.Level.REDACTED_REQUEST, "Client " + args[1] + " requested push.");
                }
                else {
                    Server.logger.log(Logger.Level.REQUEST, message);
                }
                Server.logger.log(Logger.Level.INFO, "Client requested push. Verifying credentials...");
                File profiles = new File(".server/profiles");
                for(File file : profiles.listFiles()) {
                    String name = file.getName().split("\\.")[0];
                    if(name.equals(args[1])) {
                        PropertiesFile profile = new PropertiesFile(".server/profiles/" + name + ".properties");
                        if(args[2].equals(profile.getProperty("password"))) {
                            Server.logger.log(Logger.Level.INFO, "Credentials verified.");
                            Server.logger.log(Logger.Level.INFO, "Receiving files...");

                            Dir info = (Dir) new Serialized(args[3]).deSerialize();
                            Files.clearDir(new File(".server/files/" + name));
                            for(FileInfo fileInfo : info.files) {
                                if(fileInfo.path.substring(2).contains("/")) {
                                    String[] folders = fileInfo.path.split("/");
                                    String[] newFolders = Arrays.copyOf(folders, folders.length - 1);
                                    String path = "";
                                    for(String folder : newFolders) {
                                        path += folder + "/";
                                    }
                                    path = ".server/files/" + name + File.separator + path.substring(1, path.length() - 1);
                                    File make = new File(path);
                                    make.mkdirs();
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(".server/files/" + name + File.separator + fileInfo.path);
                                fileOutputStream.write(fileInfo.data);

                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                            String response = "SUCCESS";
                            dataOutputStream.writeUTF(response);
                            Server.logger.log(Logger.Level.RESPONSE, response);
                        }
                        else {
                            Server.logger.log(Logger.Level.INFO, "Client requested fetch using invalid credentials.");
                            String response = "INVALID";
                            dataOutputStream.writeUTF(response);
                            Server.logger.log(Logger.Level.RESPONSE, response);
                        }
                    }
                    else {
                        Server.logger.log(Logger.Level.INFO, "Client requested fetch using invalid credentials.");
                        String response = "INVALID";
                        dataOutputStream.writeUTF(response);
                        Server.logger.log(Logger.Level.RESPONSE, response);
                    }
                }
            }
            else {
                Server.logger.log(Logger.Level.REQUEST, message);
                Server.logger.log(Logger.Level.INFO, "Client request invalid.");
                String response = "INVALID";
                dataOutputStream.writeUTF(response);
                Server.logger.log(Logger.Level.RESPONSE, response);
            }

            dataInputStream.close();
            dataOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
