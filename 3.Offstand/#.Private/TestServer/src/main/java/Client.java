import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args ) {
        DataInputStream streamByServer = null;
        DataOutputStream streamToServer = null;
        Scanner scanner = new Scanner(System.in);




        try {
            //서버 접속
            Socket socket = new Socket("192.168.0.11", 8080);
            while (true) {
                System.out.println("클라이언트 1");
                streamByServer = new DataInputStream(socket.getInputStream());
                System.out.println("클라이언트 2");
                streamToServer = new DataOutputStream(socket.getOutputStream());
                System.out.println("클라이언트 3");
//                JsonObject jsonObject = new JsonObject();
//                JsonObject client = new JsonObject();
//                client.addProperty("name","Mr.moon");
//                client.addProperty("character",3);
//                client.addProperty("win",15);
//                client.addProperty("lose",15);
//                client.addProperty("winningRate",50.0);
//                jsonObject.addProperty("no",1);
//                jsonObject.add("client",client);
//                streamToServer.writeUTF(jsonObject.toString());
                streamToServer.writeUTF(scanner.nextLine());
                System.out.println("클라이언트 4");
                System.out.println("Server : " + streamByServer.readUTF());

            }
        }
        catch( Exception e ){
            e.printStackTrace();

        }
    }
}
//{ "no": 1,"client": {"name": "Mr.Moon","character": 3,"win": 15,"lose": 15,"winningRate": 50.0}}