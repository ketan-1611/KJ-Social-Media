package com.example.kjsocialmedia.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kjsocialmedia.Models.MessageModel;
import com.example.kjsocialmedia.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatBoxAdapter extends RecyclerView.Adapter{


    ArrayList<MessageModel>list;
    Context context;
    String recId;

    public ChatBoxAdapter(ArrayList<MessageModel> list, Context context, String recId) {
        this.list = list;
        this.context = context;
        this.recId = recId;
    }

    int outgoing_view_type = 1;
    int incoming_view_type = 2;


    @Override
    public int getItemViewType(int position) {

        if((list.get(position).getUserId()!=null) && list.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return outgoing_view_type;
        }
        else
        {
            return incoming_view_type;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == outgoing_view_type)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.outgoing_chat,parent,false);
            return new OutgoingMessageViewholder(view);

        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.incoming_chat,parent,false);
            return new IncomingMessageViewholder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model = list.get(position);

        if (holder.getClass()==OutgoingMessageViewholder.class)
        {
            ((OutgoingMessageViewholder)holder).outgoingMess.setText(model.getMessage());
            Date date = Calendar.getInstance().getTime();
            ((OutgoingMessageViewholder)holder).outgoingTime.setText(date+"");
        }
        else
        {
            ((IncomingMessageViewholder)holder).incomingMess.setText(model.getMessage());
            String time = TimeAgo.using(model.getTime());
            ((IncomingMessageViewholder)holder).incomingTime.setText(time);
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this ? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;

                                database.getReference()
                                        .child("Chats")
                                        .child(senderRoom)
                                        .child(model.getMessageId())
                                        .setValue(null);



                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OutgoingMessageViewholder extends RecyclerView.ViewHolder {
        TextView outgoingMess,outgoingTime;
        public OutgoingMessageViewholder(@NonNull View itemView) {
            super(itemView);

            outgoingMess = itemView.findViewById(R.id.outgoingMessage);
            outgoingTime = itemView.findViewById(R.id.outgoingTime);
        }


    }

    public class IncomingMessageViewholder extends RecyclerView.ViewHolder {
        TextView incomingMess,incomingTime;
        public IncomingMessageViewholder(@NonNull View itemView) {
            super(itemView);
            incomingMess = itemView.findViewById(R.id.incomingMessage);
            incomingTime = itemView.findViewById(R.id.incomingTime);
        }
    }
}
