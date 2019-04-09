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

/**
 * 한줄로 정리해서 보내기
 * {"no" : 1,result : false,cause : 0}                                                                                                                           1. 대기실 입장 Response (자리없음)
 * {"no" : 1,"client" : [{"status" : 0,"name" : "Mr.Moon""seatNo" : 1,"character" : 7}, {"status" : 1,"name" : "이승철""seatNo" : 2,"character" : 3}, ... ]}       1. 대기실 입장 Response (대기실 입장 성공)
 * {"no" : 2,"client" : {"name" : "쫑미니","seatNo" : 3,"character" : 4}}                                                                                          3. 플레이어 대기실 입장 전달
 * {"no": 3,"seatNo1": 3,"seatNo2": 3}                                                                                                                           7. 플레이어 자리바꾸기 전달
 */

