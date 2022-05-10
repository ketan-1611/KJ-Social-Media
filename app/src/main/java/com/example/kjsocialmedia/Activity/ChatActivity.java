package com.example.kjsocialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.kjsocialmedia.Adapters.ChatAdapter;
import com.example.kjsocialmedia.Models.ChatModel;
import com.example.kjsocialmedia.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {


    ActivityChatBinding binding;
    ArrayList<ChatModel>list;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar3);
        ChatActivity.this.setTitle("Kj Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        list = new ArrayList<>();

       // list.add(new ChatModel(R.drawable.kanikas,"kanika","hye"));
        //list.add(new ChatModel(R.drawable.kanikas,"kanika","hye"));
      //  list.add(new ChatModel(R.drawable.kanikas,"kanika","hye"));


        ChatAdapter adapter = new ChatAdapter(list,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRV.setLayoutManager(layoutManager);
        binding.chatRV.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            ChatModel model = dataSnapshot.getValue(ChatModel.class);
                            model.setFollowerId(dataSnapshot.getKey());
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}