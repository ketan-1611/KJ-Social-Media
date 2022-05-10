package com.example.kjsocialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kjsocialmedia.R;
import com.example.kjsocialmedia.databinding.ActivityChatBoxBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatBoxActivity extends AppCompatActivity {

    ActivityChatBoxBinding binding;
    Intent intent;
    FirebaseDatabase database;
    ArrayList<MessageModel>messageModels;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();
        intent = getIntent();
        final String senderId = FirebaseAuth.getInstance().getUid();
        final String recieverId = intent.getStringExtra("followerId");
       // String profileImage = intent.getStringExtra("profile");
      //  String name = intent.getStringExtra("name");

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(recieverId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfile())
                                .placeholder(R.drawable.userinsta)
                                .into(binding.profileImage);
                        binding.name.setText(users.getUsername());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatBoxActivity.this, ChatActivity.class);
                finish();
                startActivity(intent);

            }
        });


        final String senderRoom = senderId + recieverId;
        final String recieverRoom = recieverId + senderId;

        messageModels = new ArrayList<>();
        final ChatBoxAdapter chatBoxAdapter = new ChatBoxAdapter(messageModels,this,recieverId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatBoxRV.setLayoutManager(layoutManager);
        binding.chatBoxRV.setAdapter(chatBoxAdapter);

        database.getReference()
                .child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());

                            messageModels.add(model);
                        }
                        chatBoxAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etmessage.getText().toString();
                if (message.isEmpty())
                {
                    binding.etmessage.setError("Please enter message");
                }

                MessageModel messageModel = new MessageModel(message,senderId);
                messageModel.setTime(new Date().getTime());

                binding.etmessage.setText("");

                database.getReference()
                        .child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messageModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("Chats")
                                        .child(recieverRoom)
                                        .push()
                                        .setValue(messageModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });

            }
        });




    }
}