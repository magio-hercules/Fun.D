import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static ServerSocket serverSocket;
    private static List<ServerThread> serverThreads = new ArrayList<>();
    private static int roomMaxAttendee = 2;

    public static void main(String[] args) {

        try {
            Socket socket = null;
            serverSocket = new ServerSocket(8080);
            System.out.println("서버소켓 생성");
            while (serverThreads.size() != roomMaxAttendee) {
                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads.add(serverThread);
                System.out.println(serverThreads.size() + "번째 클라이언트 소켓 연결 성공");
                Thread.sleep(5000);
                serverSocket.close();
            }


        } catch (InterruptedException | IOException e) {
            System.out.println("서버소켓 생성 실패 " + e.getMessage());
        }
        System.out.println("서버스레드 종료");
    }

//    private void broadcastMessage() {
//        for (ServerThread serverThread : serverThreads) {
//            serverThread.getStreamToClient().writeUTF(message);
//        }
//    }
}
