package com.example.kjsocialmedia.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kjsocialmedia.Adapters.NotificationAdapter;
import com.example.kjsocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {

    RecyclerView notificationRv;
    ArrayList<NotificationModel>list;

    FirebaseDatabase database;


    public Notification2Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification2, container, false);

        notificationRv = view.findViewById(R.id.notificationRV);

        list = new ArrayList<>();

       /* list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));
        list.add(new NotificationModel(R.drawable.kanikas,"<b>Kanika Jain</b> you recieved new message from ketan jain and also geted 55 likes","09.35pm" ));*/




        NotificationAdapter notificationAdapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRv.setLayoutManager(layoutManager);
        notificationRv.setAdapter(notificationAdapter);

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            NotificationModel model = dataSnapshot.getValue(NotificationModel.class);
                            model.setNotificationId(dataSnapshot.getKey());
                            list.add(model);

                        }
                        notificationAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}