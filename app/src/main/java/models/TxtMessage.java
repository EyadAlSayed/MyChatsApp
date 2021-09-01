package models;

import ui.MainActivity;

public class TxtMessage {

    private String from;
    private String to;
    private String text;

    public TxtMessage() {
    }

    public TxtMessage(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;

    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromMe(){
        return MainActivity.getDeviceIp().equals(this.from);
    }

    public  static TxtMessage getReceivedMessageFormat(String message) {
        String []parts = message.split("-");
        if(parts.length >= 3) {

            /*  from , To , textMessage **/
            return new TxtMessage(parts[0], parts[1], parts[2]);
        }
        return null;
    }

    public   String getSendMessageFormat(){
        return this.from+"-"+this.to+"-"+this.text;
    }
}
