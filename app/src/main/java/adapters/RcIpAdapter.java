package adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import models.MainActivityCallBack;
import ui.ChatActivity;
import ui.MainActivity;
import ui.R;

public class RcIpAdapter  extends RecyclerView.Adapter<RcIpAdapter.InnerRc> {

    private  ArrayList<String> ipList;
    private MainActivityCallBack mainActivityCallBack;

    public  void setIpList(ArrayList<String> ipList) {
        this.ipList=ipList;
    }
    public void setMainActivityCallBack(MainActivityCallBack callBack) { this.mainActivityCallBack = callBack; }

    @NonNull
    @Override
    public InnerRc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_ip_items,parent,false);
        return new InnerRc(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRc holder, int position) {
        holder.callBack = mainActivityCallBack;
        holder.ip_txt.setText(ipList.get(position));
        holder.ip_txt.setOnClickListener(holder.onIpClick);


    }

    @Override
    public int getItemCount() {
        if (ipList == null) return 0;
        return ipList.size();
    }

    public static class InnerRc extends RecyclerView.ViewHolder {

        public TextView ip_txt;
        private String ip;
        private MainActivityCallBack callBack;
        
        public InnerRc(@NonNull View view) {
            super(view);
            ip_txt = view.findViewById(R.id.ip_item);
        }

        public  View.OnClickListener onIpClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = ip_txt.getText().toString();
                openNewActivity();
            }
        };
        private void openNewActivity(){
            Intent intent = new Intent(MainActivity.getContext(), ChatActivity.class);
            intent.putExtra("IP",ip);
            MainActivity.getContext().startActivity(intent);
            callBack.slideToAnOtherActivity();
        }
    }
}
