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
                streamByClient = new DataInputStream(socket.getInputStream());
                streamToClient = new DataOutputStream(socket.getOutputStream());
                String client = streamByClient.readUTF();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(client);
                int no = jsonObject.get("no").getAsInt();
                System.out.println("Client " + socket.getRemoteSocketAddress() + " >> " + jsonObject);
                System.out.println("Server >> ");
                streamToClient.writeUTF(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("에러 " + e.getMessage());
        }
    }

}
