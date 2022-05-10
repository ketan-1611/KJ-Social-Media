package com.example.kjsocialmedia.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kjsocialmedia.Adapters.UserAdapter;
import com.example.kjsocialmedia.Models.Users;
import com.example.kjsocialmedia.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    ArrayList<Users>list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;


    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentSearchBinding.inflate(inflater, container, false);

        UserAdapter userAdapter = new UserAdapter(list,getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.searchRV.setLayoutManager(layoutManager);
        binding.searchRV.setAdapter(userAdapter);




        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Users users = snapshot1.getValue(Users.class);
                    users.setUserId(snapshot1.getKey());
                    if(!(snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())))
                    list.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         return binding.getRoot();
    }
}