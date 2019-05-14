import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        DataInputStream streamByServer = null;
        DataOutputStream streamToServer = null;
        Scanner scanner = new Scanner(System.in);

        try {
            //서버 접속
            Socket socket = new Socket("192.168.0.3", 8080);
            System.out.println("클라이언트 소켓 " + socket.getLocalPort());
            while (true) {
//                streamByServer = new DataInputStream(socket.getInputStream());
                streamToServer = new DataOutputStream(socket.getOutputStream());
                System.out.println("Client >> ");
                streamToServer.writeUTF(scanner.nextLine());
//                System.out.println("Server : " + streamByServer.readUTF());
            }
        } catch (Exception e) {
            System.out.println("클라이언트 실패 " + e.getMessage());
        }
    }
}

/**
 * 한줄로 정리해서 보내기
 *
 * 대기실 입장
 * {"user":{"avatar":3,"lose":5,"name":"테스트","status":0,"win":1},"no":1}
 *
 * 레디
 * {"no":4,"seatNo":2}
 * 레디 취소
 * {"no":6,"seatNo":2}
 * 죽기
 * {"no":9,"seatNo":2}
 * 카드 오픈
 * {"no":11,"seatNo":2}
 * 셔플
 * {"no":14}
 * 나가기
 * {"no":13,"seatNo":2}
 * 레디 취소
 * {"no":15,"seatNo":2}
 */