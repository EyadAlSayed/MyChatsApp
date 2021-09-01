package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import adapters.RcIpAdapter;
import models.MainActivityCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sockets.ChatSocket;
import tech.gusavila92.websocketclient.WebSocketClient;


public class MainActivity extends AppCompatActivity implements MainActivityCallBack {

    private static Context context;
    private static String deviceIp;
    private static ChatSocket chatSocket;

    private RcIpAdapter rcIpAdapter;
    private ArrayList<String>ipList;
    private EditText ip_input;

    private RestAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        context = this;


        INI_restAPI();
        INI_webSocket();
        INI_Rc();


        Button scanIp = findViewById(R.id.scanIp_btn);
        Button shMsg = findViewById(R.id.shMsg_btn);
        Button connect = findViewById(R.id.connect_btn);
        ip_input = findViewById(R.id.ip_input);

        ip_input.addTextChangedListener(onIpeEntered);
        connect.setEnabled(false);
        scanIp.setOnClickListener(onScanIpClick);
        shMsg.setOnClickListener(openNextActivity);
        connect.setOnClickListener(onConnectPressed);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public static Context getContext(){return context;}

    private final View.OnClickListener onScanIpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         if(ipList != null) ipList.clear();
            getAllIp();
        }
    };
    private final View.OnClickListener openNextActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(),ChatActivity.class);
            intent.putExtra("shMSG","");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        };



    private final View.OnClickListener onConnectPressed  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getContext(),ChatActivity.class);
            intent.putExtra("IP",ip_input.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            ip_input.getText().clear();
        }
    };


    private final TextWatcher onIpeEntered = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String []part = s.toString().split("\\.");
            if(part.length == 4) findViewById(R.id.connect_btn).setEnabled(true);
        }

        };

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();

                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
        return "";
    }

    public static String getDeviceIp(){
        if(deviceIp == null)
        {
            deviceIp = getIPAddress(true);
        }
        return deviceIp;
    }

    private void getAllIp()  {

        api.getAllIp().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if(response.isSuccessful()) {
                    ipList = (ArrayList<String>) response.body();
                    ipList.remove(getDeviceIp());
                    rcIpAdapter.setIpList(ipList);
                    rcIpAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });

    }

    public static  ChatSocket getChatSocket(){
        if(chatSocket == null)
        {
            String BASE_URI = "ws://192.168.1.101:3030/myChat";
            chatSocket = new ChatSocket(URI.create(BASE_URI));
        }
        return chatSocket;
    }


    private void INI_Rc(){

        RecyclerView rc = findViewById(R.id.rc_ip);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rcl = new LinearLayoutManager(this);
        rcIpAdapter = new RcIpAdapter();
        rcIpAdapter.setMainActivityCallBack(this);
        rc.setLayoutManager(rcl);
        rc.setAdapter(rcIpAdapter);
    }

    private  void  INI_restAPI(){
        String BASE_URL = "http://192.168.1.101:3030";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(RestAPI.class);
    }


    private void INI_webSocket(){
        getChatSocket().connect();
    }


    @Override
    public void slideToAnOtherActivity() {
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
