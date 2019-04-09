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
            Socket socket = new Socket("192.168.40.14", 8080);
            System.out.println("클라이언트 소켓 " + socket.getLocalPort());
            while (true) {
                streamByServer = new DataInputStream(socket.getInputStream());
                streamToServer = new DataOutputStream(socket.getOutputStream());
                System.out.println("Client >> ");
                streamToServer.writeUTF(scanner.nextLine());
                System.out.println("Server : " + streamByServer.readUTF());
            }
        }
        catch( Exception e ){
            System.out.println("클라이언트 실패 " + e.getMessage());
        }
    }
}
//{ "no": 1,"client": {"name": "Mr.Moon","character": 3,"win": 15,"lose": 15,"winningRate": 50.0}}