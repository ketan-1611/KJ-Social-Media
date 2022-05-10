package com.example.kjsocialmedia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kjsocialmedia.Activity.CommentActivity;
import com.example.kjsocialmedia.Models.NotificationModel;
import com.example.kjsocialmedia.Models.Users;
import com.example.kjsocialmedia.R;
import com.example.kjsocialmedia.databinding.NotificationRvDesignBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<NotificationModel>list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel model = list.get(position);

        String type = model.getType();

        String time = TimeAgo.using(model.getNotificationAt());
        holder.binding.time.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfile())
                                .placeholder(R.drawable.userinsta)
                                .into(holder.binding.profileImage);

                        if(type.equals("like"))
                        {
                            holder.binding.notification.setText(Html.fromHtml("<b> " + users.getUsername() + "</b>" + " liked your post" ));

                        }
                        else if(type.equals("comment"))
                        {
                            holder.binding.notification.setText(Html.fromHtml("<b> " + users.getUsername() + "</b>" + " commented your post" ));
                        }
                        else
                        {
                            holder.binding.notification.setText(Html.fromHtml("<b> " + users.getUsername() + "</b>" + " started following you" ));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.notificationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!type.equals("follow")){


                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(model.getPostedBy())
                            .child(model.getNotificationId())
                            .child("checkOpen")
                            .setValue(true);


                    holder.binding.notificationClick.setBackgroundColor(Color.parseColor("#000000"));
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId",model.getPostId());
                    intent.putExtra("postedBy",model.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }




            }
        });

        boolean checkOpen = model.isCheckOpen();
        if (checkOpen==true)
        {
            holder.binding.notificationClick.setBackgroundColor(Color.parseColor("#000000"));
        }
        else
        {

        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        NotificationRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = NotificationRvDesignBinding.bind(itemView);


        }
    }
}
