package ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import adapters.RcChatAdapter;
import models.ChatActivityCallBack;
import models.TxtMessage;
import sockets.ChatSocket;



public class ChatActivity extends AppCompatActivity implements ChatActivityCallBack {
    private static Context context;
    private ChatSocket chatSocket;
    private EditText msg_input;
    private String ip_receiver;
    private Button send;
    public RcChatAdapter rcChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chat_activity);

        context = this;

        INI_webSocket();

        INI_RC();


        ip_receiver = getIntent().getExtras().getString("IP", null);


        send = findViewById(R.id.send_btn);
        msg_input = findViewById(R.id.msg_input);
        msg_input.addTextChangedListener(onMsgEntered);
        send.setEnabled(false);
        send.setOnClickListener(onSend);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public static Context getContext () {
        return context;
    }

    private final View.OnClickListener onSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(ip_receiver == null )
            {
                Toast.makeText(getApplicationContext(),"You have to choose an ip to send message",Toast.LENGTH_LONG).show();
            }
            else {
                TxtMessage txtMessage = new TxtMessage(MainActivity.getDeviceIp(), ip_receiver, msg_input.getText().toString());
                chatSocket.getMessages().add(txtMessage);
                chatSocket.send(txtMessage.getSendMessageFormat());
                rcChatAdapter.notifyDataSetChanged();
                msg_input.getText().clear();
            }

        }
    };
    private final TextWatcher  onMsgEntered = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString().trim();
            if(!str.isEmpty()) send.setEnabled(true);
        }
    };

    private void INI_RC () {
        RecyclerView rc = findViewById(R.id.rc_chat);
        rc.setHasFixedSize(true);
        RecyclerView.LayoutManager rcl = new LinearLayoutManager(this);
        rcChatAdapter = new RcChatAdapter();
        rcChatAdapter.setMessages(chatSocket.getMessages());
        rc.setLayoutManager(rcl);
        rc.setAdapter(rcChatAdapter);
    }
    private void INI_webSocket () {
       chatSocket = MainActivity.getChatSocket();
       chatSocket.setCallBack(this);
    }

    @Override
    public void checkForNewMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rcChatAdapter.notifyDataSetChanged();
            }
        });
        Log.i("Recyceler Adapter","new message has been received");
    }
}
