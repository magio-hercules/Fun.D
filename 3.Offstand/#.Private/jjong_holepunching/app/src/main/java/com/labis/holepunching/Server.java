package com.labis.holepunching;


import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
    String message = "";
//    static final int socketServerPORT = 8080;

    Thread socketServerThread;
    SocketServerReplyThread socketServerReplyThread;

    String socketServerIP;
    int socketServerPORT;

    public Server(MainActivity activity, String ip, String port) {
        Log.d("TEST", "Server 생성자 호출 ip : " + ip + ", port : " + port + ")");

        this.activity = activity;

        socketServerIP = ip;
        socketServerPORT = Integer.parseInt(port);

        socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (socketServerReplyThread != null) {
            socketServerReplyThread.stop();
            socketServerReplyThread = null;
        }

        if (socketServerThread != null) {
            socketServerThread.stop();
            socketServerThread = null;
        }

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
//                serverSocket = new ServerSocket(socketServerPORT);
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(socketServerPORT));
//                serverSocket.bind(new InetSocketAddress(socketServerIP, socketServerPORT));
                Log.d("TEST", "serverSocket bind 완료");

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";
                    Log.d("TEST", "serverSocket.accept message : " + message);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            activity.msg.setText(message);
                            activity.tvMain.setText(activity.tvMain.getText().toString() + "server  message : " + message + "\n");
                        }
                    });

                    socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            try {
//                Log.d("TEST", "서버 오픈됨");
//                serverSocket.close();
//                serverSocket = new ServerSocket();
//                serverSocket.setReuseAddress(true);
//                serverSocket.bind(new InetSocketAddress(socketServerPORT));
//
//                Log.d("TEST", "serverSocket 재 bind 완료");
//
//                while (true) {
//                    // block the call until connection is created and return
//                    // Socket object
//                    Socket socket = serverSocket.accept();
//                    count++;
//                    message += "#" + count + " from "
//                            + socket.getInetAddress() + ":"
//                            + socket.getPort() + "\n";
//                    Log.d("TEST", "serverSocket.accept message : " + message);
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            activity.msg.setText(message);
//                            activity.tvMain.setText(activity.tvMain.getText().toString() + "server  message : " + message + "\n");
//                        }
//                    });
//
//                    socketServerReplyThread =
//                            new SocketServerReplyThread(socket, count);
//                    socketServerReplyThread.run();
//
//                }
//            }catch(IOException e){e.printStackTrace();}
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Hello from Server, you are #" + cnt;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n";

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        activity.msg.setText(message);
                        activity.tvMain.setText(activity.tvMain.getText().toString() + "server  message : " + message + "\n");
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    activity.msg.setText(message);
                    activity.tvMain.setText(activity.tvMain.getText().toString() + "server  message : " + message + "\n");
                }
            });
        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}