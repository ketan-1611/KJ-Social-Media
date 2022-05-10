package com.example.kjsocialmedia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kjsocialmedia.Activity.CommentActivity;
import com.example.kjsocialmedia.Models.NotificationModel;
import com.example.kjsocialmedia.Models.PostModel;
import com.example.kjsocialmedia.Models.Users;
import com.example.kjsocialmedia.R;
import com.example.kjsocialmedia.databinding.DashboardRvDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<PostModel>list;
    Context context;

    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_design,parent,false);

        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PostModel model = list.get(position);

        Picasso.get()
                .load(model.getPostImg())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.postImg);

        holder.binding.like.setText(model.getPostlikes()+"");
        holder.binding.comment.setText(model.getCommentsCount()+"");

        String description = model.getPostDesc();
        if(description.equals(""))
        {
            holder.binding.postDesc.setVisibility(View.GONE);

        }else {
            holder.binding.postDesc.setText(description);
            holder.binding.postDesc.setVisibility(View.VISIBLE);
        }

        holder.binding.postDesc.setText(model.getPostDesc());

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfile())
                        .placeholder(R.drawable.userinsta)
                        .into(holder.binding.profileImage);
                holder.binding.userName.setText(users.getUsername());
                holder.binding.about.setText(users.getProfession());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like,0,0,0);


                        }
                        else
                        {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(model.getPostId())
                                                            .child("postlikes")
                                                            .setValue(model.getPostlikes() + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like,0,0,0);


                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setPostId(model.getPostId());
                                                                    notificationModel.setPostedBy(model.getPostedBy());
                                                                    notificationModel.setType("like");


                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(model.getPostedBy())
                                                                            .push()
                                                                            .setValue(notificationModel);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPostId());
                intent.putExtra("postedBy",model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                context.startActivity(intent);
            }
        });







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        DashboardRvDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvDesignBinding.bind(itemView);


        }
    }
}
