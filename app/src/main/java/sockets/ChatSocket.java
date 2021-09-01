package sockets;

import android.util.Log;

import java.net.URI;
import java.util.ArrayList;

import models.ChatActivityCallBack;
import models.TxtMessage;
import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatSocket extends WebSocketClient {
    /**
     * Initialize all the variables
     *
     * @param uri URI of the WebSocket server
     */
    private URI uri;
    private final ArrayList<TxtMessage> messages = new ArrayList<>();
    private TxtMessage last_message;
    private byte[] binData;
    private byte[] pingData;
    private byte[] pongData;
    private ChatActivityCallBack callBack ;


    public ChatSocket(URI uri) {
        super(uri);
        this.uri = uri;
    }

    public void setCallBack(ChatActivityCallBack callBack) {
        this.callBack = callBack;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public void onOpen() {
        Log.i("webSocket", "onOpen");
    }

    @Override
    public void onTextReceived(String message) {

        TxtMessage msg = TxtMessage.getReceivedMessageFormat(message);
        Log.i("webSocket", "onTextReceived : [" + msg.getText() + "]");

        last_message = msg;
        messages.add(msg);
        callBack.checkForNewMessage();
    }

    @Override
    public void onBinaryReceived(byte[] data) {
        Log.i("webSocket", "onBinaryReceived");
        this.binData = data;
    }

    @Override
    public void onPingReceived(byte[] data) {
        Log.i("webSocket", "onPingReceived");
        this.pingData = data;
    }

    @Override
    public void onPongReceived(byte[] data) {
        Log.i("webSocket", "onPongReceived");
        this.pongData = data;
    }

    @Override
    public void onException(Exception e) {
        Log.i("webSocket", e.toString());
    }

    @Override
    public void onCloseReceived() {
        Log.d("webSocket", "onClosed");
    }


    public TxtMessage getMessage() {
        return last_message;
    }

    public byte[] getBinData() {
        return binData;
    }

    public byte[] getPingData() {
        return pingData;
    }

    public byte[] getPongData() {
        return pongData;
    }

    public  ArrayList<TxtMessage> getMessages() {
        return messages;
    }

}