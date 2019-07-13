package com.labis.holepunching;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.net.URISyntaxException;

//import io.socket.client.IO;
//import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    public TextView tvMain;

    private Client socketClient;
    private Server hostServer;

    private EditText editServerIp;
    private EditText editServerPort;


    private RadioGroup radioGroup;
    private Button buttonConnect;
    private Button buttonDisonnect;

    String localAddressS;
    String localPortS;
    String remoteAddressS;
    String remotePortS;
    String localAddressC;
    String localPortC;
    String remoteAddressC;
    String remotePortC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMain = findViewById(R.id.tvMain);

        editServerIp = findViewById(R.id.editServerIp);
        editServerPort = findViewById(R.id.editServerPort);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonDisonnect = (Button) findViewById(R.id.buttonDisconnect);


        initListener();

    }


    public void initListener() {

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST","Connect");
//                int selectedId = radioGroup.getCheckedRadioButtonId();

                connect();
            }
        });
        buttonDisonnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST","Disconnect");
                tvMain.setText(tvMain.getText().toString() + "Disconnect\n");
                socketClient.disconnect();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioHost){
                    Log.d("TEST", "HOST로 변경");

                }else if(checkedId == R.id.radioGuest){
                    Log.d("TEST", "GUEST로 변경");

                }
            }
        });
    }

    public void connect() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String myIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        String serverIp = editServerIp.getText().toString();
        int serverPort = Integer.parseInt(editServerPort.getText().toString());
        Log.d("TEST", "Server IP : " + serverIp + ":" + serverPort);

        tvMain.setText(tvMain.getText().toString() + "Server IP (" + serverIp + ":" + serverPort + ")\n");

        socketClient = new Client(serverIp, serverPort);
        socketClient.setClientCallback(new Client.ClientCallback () {
            @Override
            public void onMessage(String message) {
                Log.d("TEST", "서버로부터 받은 socketClient onMessage : " + message);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(message);
                    String name = jsonObj.getString("name");
                    Log.d("TEST", "JSONObject name : " + name);

                    String msg = "";
                    if (name.equals("A")) {
                        msg = "HOST 정보 => ";
                    } else {
                        msg = "GUEST 정보 => ";
                    }

                    final String finalMsg = msg;
                    runOnUiThread(()->{
                        tvMain.setText(tvMain.getText().toString() + finalMsg + "message : " + message + "\n");
                    });

                    String localAddress = jsonObj.getString("localAddress");
                    String localPort = jsonObj.getString("localPort");
                    String remoteAddress = jsonObj.getString("remoteAddress");
                    String remotePort = jsonObj.getString("remotePort");
                    Log.d("TEST", "JSONObject localAddress : " + localAddress);
                    Log.d("TEST", "JSONObject localPort : " + localPort);
                    Log.d("TEST", "JSONObject remoteAddress : " + remoteAddress);
                    Log.d("TEST", "JSONObject remotePort : " + remotePort);

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioHost) {
                        if (name.equals("A")) {
                            localAddressS = localAddress;
                            localPortS = localPort;
                            remoteAddressS = remoteAddress;
                            remotePortS = remotePort;
                        } else if (name.equals("B")) {
                            localAddressC = localAddress;
                            localPortC = localPort;
                            remoteAddressC = remoteAddress;
                            remotePortC = remotePort;

                            // step 1 localAddress로 listen
                            Log.d("TEST", "hostServer 생성 (localAddress)");
//                            hostServer = new Server(MainActivity.this, localAddressS, localPortS);
//                        hostServer = new Server(MainActivity.this, remoteAddressS, remotePortS);

                            // step2 방장이 먼저 소켓 통신 시도
                            Log.d("TEST", "방장이 먼저 첫번째 소켓 통신 시도");

                            Client hostClient = new Client(remoteAddressC, Integer.parseInt(remotePortC));
                            hostClient.setClientCallback(new Client.ClientCallback () {
                                @Override
                                public void onMessage(String message) {
                                    Log.d("TEST", "서버로부터 받은 hostClient onMessage : " + message);
                                    runOnUiThread(()->{
                                        tvMain.setText(tvMain.getText().toString() + "서버로부터 받은 message message : " + message + "\n");
                                    });
                                }
                                @Override
                                public void onConnect(Socket _socket) {
                                    Log.d("TEST", "hostClient onConnect");
                                }
                                @Override
                                public void onDisconnect(Socket socket, String message) {
                                    Log.d("TEST", "hostClient onDisconnect");
                                }
                                @Override
                                public void onConnectError(Socket socket, String message) {
                                    Log.d("TEST", "hostClient onConnectError");

                                    Log.d("TEST", "hostServer.onDestroy");
                                    hostServer.onDestroy();

                                    Log.d("TEST", "hostServer 생성 (remoteAddress)");
                                    hostServer = new Server(MainActivity.this, localAddressS, localPortS);
//                                    hostServer = new Server(MainActivity.this, remoteAddressS, remotePortS);

                                    Log.d("TEST", "방장이 먼저 두번째 소켓 통신 시도");

                                    Client hostClient = new Client(localAddressC, Integer.parseInt(localPort));
                                    hostClient.setClientCallback(new Client.ClientCallback () {
                                        @Override
                                        public void onMessage(String message) {
                                            Log.d("TEST", "서버로부터 받은 hostClient onMessage : " + message);
                                            runOnUiThread(()->{
                                                tvMain.setText(tvMain.getText().toString() + "서버로부터 받은 message message : " + message + "\n");
                                            });
                                        }
                                        @Override
                                        public void onConnect(Socket _socket) {
                                            Log.d("TEST", "hostClient onConnect");
                                        }
                                        @Override
                                        public void onDisconnect(Socket socket, String message) {
                                            Log.d("TEST", "hostClient onDisconnect");
                                        }
                                        @Override
                                        public void onConnectError(Socket socket, String message) {
                                            Log.d("TEST", "hostClient onConnectError");
                                        }
                                    });

                                    hostClient.connect();
                                }
                            });

                            hostClient.connect();
                        }
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioGuest) {
                        if (name.equals("B")) {
                            localAddressS = localAddress;
                            localPortS = localPort;
                            remoteAddressS = remoteAddress;
                            remotePortS = remotePort;
                        } else if (name.equals("A")) {
                            localAddressC = localAddress;
                            localPortC = localPort;
                            remoteAddressC = remoteAddress;
                            remotePortC = remotePort;

                            // step 1 localAddress로 listen
                            Log.d("TEST", "hostServer 생성 (localAddress)");
//                            hostServer = new Server(MainActivity.this, localAddressS, localPortS);
//                            hostServer = new Server(MainActivity.this, remoteAddressS, remotePortS);

                            // step2 방장이 먼저 소켓 통신 시도
                            Log.d("TEST", "방장이 먼저 첫번째 소켓 통신 시도");

                            Client hostClient = new Client(remoteAddressC, Integer.parseInt(remotePortC));
                            hostClient.setClientCallback(new Client.ClientCallback () {
                                @Override
                                public void onMessage(String message) {
                                    Log.d("TEST", "서버로부터 받은 hostClient onMessage : " + message);
                                    runOnUiThread(()->{
                                        tvMain.setText(tvMain.getText().toString() + "서버로부터 받은 message message : " + message + "\n");
                                    });
                                }
                                @Override
                                public void onConnect(Socket _socket) {
                                    Log.d("TEST", "hostClient onConnect");
                                }
                                @Override
                                public void onDisconnect(Socket socket, String message) {
                                    Log.d("TEST", "hostClient onDisconnect");
                                }
                                @Override
                                public void onConnectError(Socket socket, String message) {
                                    Log.d("TEST", "hostClient onConnectError");

                                    Log.d("TEST", "hostServer.onDestroy");
                                    hostServer.onDestroy();

                                    Log.d("TEST", "hostServer 생성 (remoteAddress)");
//                                    hostServer = new Server(MainActivity.this, localAddressS, localPortS);
                                    hostServer = new Server(MainActivity.this, remoteAddressS, remotePortS);

                                    Log.d("TEST", "방장이 먼저 두번째 소켓 통신 시도");

                                    Client hostClient = new Client(localAddressC, Integer.parseInt(localPortC));
                                    hostClient.setClientCallback(new Client.ClientCallback () {
                                        @Override
                                        public void onMessage(String message) {
                                            Log.d("TEST", "서버로부터 받은 hostClient onMessage : " + message);
                                            runOnUiThread(()->{
                                                tvMain.setText(tvMain.getText().toString() + "서버로부터 받은 message message : " + message + "\n");
                                            });
                                        }
                                        @Override
                                        public void onConnect(Socket _socket) {
                                            Log.d("TEST", "hostClient onConnect");
                                        }
                                        @Override
                                        public void onDisconnect(Socket socket, String message) {
                                            Log.d("TEST", "hostClient onDisconnect");
                                        }
                                        @Override
                                        public void onConnectError(Socket socket, String message) {
                                            Log.d("TEST", "hostClient onConnectError");
                                        }
                                    });
                                    hostClient.connect();
                                }
                            });
                            hostClient.connect();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnect(Socket _socket) {
                Log.d("TEST", "_socket : " + _socket.toString());

                String _ip = _socket.getLocalAddress().toString().substring(1);
                String _port = ""+_socket.getLocalPort();

                Log.d("TEST", "_socket.getLocalAddress().toString() : " + _socket.getLocalAddress().toString());
                Log.d("TEST", "_socket.getLocalAddress().toString().substring(1) : " + _ip);
                Log.d("TEST", "_socket.getLocalPort() : " + _port);

                runOnUiThread(()->{
                    tvMain.setText(tvMain.getText().toString() + "My IP (" + _ip + ":" + _port + ")\n");
                });

                boolean bHost;
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioHost) {
                    bHost = true;
                } else {
                    bHost = false;
                }
                JSONObject jsonObject = makeJson(bHost, _ip, _port);
                socketClient.send(jsonObject.toString());
            }

            @Override
            public void onDisconnect(Socket socket, String message) {
                Log.d("TEST", "onDisconnect");
            }

            @Override
            public void onConnectError(Socket socket, String message) {
                Log.d("TEST", "onConnectError");
            }
        });

        socketClient.connect();
    }


    private JSONObject makeJson(boolean bHost, String _ip, String _port) {
        Log.d("TEST", "makeJson");
        JSONObject retVal = null;

        JsonObject preJsonObject = new JsonObject();
        if (bHost) {
            preJsonObject.addProperty("name", "A");
        } else {
            preJsonObject.addProperty("name", "B");
        }
        preJsonObject.addProperty("localAddress", _ip);
        preJsonObject.addProperty("localPort", _port);

        try {
            retVal = new JSONObject(preJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }
}

// 참고 socket.io 사용하는 방법
//public class MainActivity extends AppCompatActivity {
//    private String TAG = "MainActivity";
//    private Socket mSocket;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        try {
//            mSocket = IO.socket("http://192.168.0.16:3000");
//            mSocket.connect();
//            mSocket.on(Socket.EVENT_CONNECT, onConnect);
//            mSocket.on("serverMessage", onMessageReceived);
//        } catch(URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // Socket서버에 connect 되면 발생하는 이벤트
//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            mSocket.emit("clientMessage", "hi");
//        }
//    };
//
//    // 서버로부터 전달받은 'chat-message' Event 처리.
//    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
//            try {
//                JSONObject receivedData = (JSONObject) args[0];
//                Log.d(TAG, receivedData.getString("msg"));
//                Log.d(TAG, receivedData.getString("data"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//}
//출처: https://mainia.tistory.com/5720 [녹두장군 - 상상을 현실로]

