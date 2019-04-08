import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static ServerSocket serverSocket;
    private static List<ServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Socket socket = null;
            serverSocket = new ServerSocket(8080);
            while (true) {
                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                serverThreads.add(serverThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
