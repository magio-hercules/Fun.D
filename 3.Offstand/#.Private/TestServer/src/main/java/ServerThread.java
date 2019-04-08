import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread implements Runnable {

    private Socket socket;
    private DataInputStream streamByClient = null;
    private DataOutputStream streamToClient = null;
    private Scanner scanner = new Scanner(System.in);
    private JsonParser jsonParser = new JsonParser();

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            while (true) {
//                System.out.println("클라이언트 접속 1");
                streamByClient = new DataInputStream(socket.getInputStream());
//                System.out.println("클라이언트 접속 2");
                streamToClient = new DataOutputStream(socket.getOutputStream());
//                System.out.println("클라이언트 접속 3");
                String client = streamByClient.readUTF();
//                System.out.println("클라이언트 접속 4 " + client);
                JsonObject jsonObject = (JsonObject) jsonParser.parse(client);
                int no = jsonObject.get("no").getAsInt();
//                System.out.println("클라이언트 접속 5 " + no);
                switch (no) {
                    case 1:
                        streamToClient.writeUTF(scanner.nextLine());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;

                }
//                System.out.println("클라이언트 접속 4");
            }
        } catch (IOException e) {
            System.out.println("에러 " + e.getMessage());
        }
    }

}
