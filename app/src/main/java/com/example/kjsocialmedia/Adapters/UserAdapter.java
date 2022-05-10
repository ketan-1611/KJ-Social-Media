package com.example.kjsocialmedia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kjsocialmedia.Models.Follow;
import com.example.kjsocialmedia.Models.NotificationModel;
import com.example.kjsocialmedia.Models.Users;
import com.example.kjsocialmedia.R;
import com.example.kjsocialmedia.databinding.SearchRvDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    ArrayList<Users>list;
    Context context;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_rv_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users users = list.get(position);
        Picasso.get()
                .load(users.getCoverPhotos())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.coverImg);
        Picasso.get()
                .load(users.getProfile())
                .placeholder(R.drawable.userinsta)
                .into(holder.binding.profileImage);

        holder.binding.name.setText(users.getUsername());
        holder.binding.profession.setText(users.getProfession());


        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(users.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            holder.binding.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_btn_active));
                            holder.binding.btnFollow.setText("Following");
                            holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.green));
                            holder.binding.btnFollow.setEnabled(false);
                        }
                        else{
                            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Follow follow = new Follow();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(new Date().getTime());


                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(users.getUserId())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(users.getUserId())
                                                    .child("followerCount")
                                                    .setValue(users.getFollowerCount() + 1)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            holder.binding.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_btn_active));
                                                            holder.binding.btnFollow.setText("Following");
                                                            holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.green));
                                                            holder.binding.btnFollow.setEnabled(false);
                                                            Toast.makeText(context, "you Followed" +users.getUsername(), Toast.LENGTH_SHORT).show();


                                                            NotificationModel notificationModel = new NotificationModel();
                                                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                            notificationModel.setNotificationAt(new Date().getTime());
                                                            notificationModel.setType("follow");


                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("notification")
                                                                    .child(users.getUserId())
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



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        SearchRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SearchRvDesignBinding.bind(itemView);

        }
    }
}
