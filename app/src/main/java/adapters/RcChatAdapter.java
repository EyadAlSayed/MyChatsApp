package adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import models.TxtMessage;
import ui.R;

public class RcChatAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<TxtMessage> messages;
    public static final  int MESSAGE_TYPE_OUT = 1;
    public  static  final  int MESSAGE_TYPE_IN = 2;


    public void setMessages(ArrayList<TxtMessage> messages) {
        this.messages = messages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) return  null;
        else
        if(viewType == MESSAGE_TYPE_IN){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_message,parent,false);
            return new InMessage(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.out_message,parent,false);
            return new OutMessage(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TxtMessage msg = messages.get(position);
        if (msg.isFromMe()) {
            ((OutMessage) holder).bind(position);
        } else {
            ((InMessage) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if(messages == null) return 0;
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages == null) return 0;
        if(messages.get(position).isFromMe())
            return 1;
        return 2;
    }

    public  class OutMessage extends RecyclerView.ViewHolder{

        public TextView txt_msg;
        public TextView txt_time;

        public OutMessage(@NonNull View itemView) {
            super(itemView);
            txt_msg = itemView.findViewById(R.id.my_msg);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
        public void bind(int pos)
        {
            TxtMessage msg = messages.get(pos);
            txt_msg.setText(msg.getText());
            txt_time.setText(getLocalTime());
        }
        private String getLocalTime(){
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            return df.format(Calendar.getInstance().getTime());
        }
    }

    public  class InMessage extends RecyclerView.ViewHolder {

        public TextView txt_msg;
        public TextView txt_time;

        public InMessage(@NonNull View itemView) {
            super(itemView);
            txt_msg = itemView.findViewById(R.id.received_msg);
            txt_time = itemView.findViewById(R.id.txt_time);
        }

        public void bind(int pos) {
            TxtMessage msg = messages.get(pos);
            txt_msg.setText("From : " + msg.getFrom() + "\n" + msg.getText());
            txt_time.setText(getLocalTime());
        }

        private String getLocalTime() {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            return df.format(Calendar.getInstance().getTime());
        }
    }

}
