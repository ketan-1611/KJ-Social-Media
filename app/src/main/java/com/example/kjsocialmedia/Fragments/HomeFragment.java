package com.example.kjsocialmedia.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kjsocialmedia.Adapters.PostAdapter;
import com.example.kjsocialmedia.Adapters.StoryAdapter;
import com.example.kjsocialmedia.Activity.ChatActivity;
import com.example.kjsocialmedia.Models.PostModel;
import com.example.kjsocialmedia.Models.StoryModel;
import com.example.kjsocialmedia.Models.UserStories;
import com.example.kjsocialmedia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    //FragmentHomeBinding binding;
    RecyclerView storyRV,dashboardRV;
    ArrayList<StoryModel> list;
    ArrayList<PostModel>postModelArrayList;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView addStoryImage,chatArea;

    ActivityResultLauncher<String> galleryLauncher;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //binding = FragmentHomeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);



        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        chatArea = view.findViewById(R.id.chatArea);

        chatArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });


        storyRV = view.findViewById(R.id.storyRV);
        list = new ArrayList<>();

      //  list.add(new StoryModel(R.drawable.kanikas,R.drawable.ic_baseline_videocam_24,R.drawable.kanikas,"Kanika Jain"));

        StoryAdapter adapter = new StoryAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRV.setLayoutManager(layoutManager);
        storyRV.setNestedScrollingEnabled(false);
        storyRV.setAdapter(adapter);

        database.getReference().child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            list.clear();
                            for (DataSnapshot storysnapshot : snapshot.getChildren())
                            {
                                StoryModel storyModel = new StoryModel();
                                storyModel.setStoryBy(storysnapshot.getKey());
                                storyModel.setStoryAt(storysnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories> stories = new ArrayList<>();
                                for (DataSnapshot snapshot1 : storysnapshot.child("userStories").getChildren())
                                {
                                    UserStories userStories = snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                storyModel.setStories(stories);
                                list.add(storyModel);


                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        dashboardRV = view.findViewById(R.id.dashboardRV);
        postModelArrayList = new ArrayList<>();


        PostAdapter dashboardAdapter = new PostAdapter(postModelArrayList,getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager1);
        dashboardRV.setAdapter(dashboardAdapter);


        database.getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postModelArrayList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            PostModel post = snapshot1.getValue(PostModel.class);
                            post.setPostId(snapshot1.getKey());
                            postModelArrayList.add(post);
                        }
                        dashboardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


         addStoryImage = view.findViewById(R.id.addStoryImage);
         addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galleryLauncher.launch("image/*");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        addStoryImage.setImageURI(result);

                        final StorageReference reference = storage.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime()+"");
                                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                StoryModel storyModel = new StoryModel();
                                                storyModel.setStoryAt(new Date().getTime());
                                                database.getReference()
                                                        .child("stories")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("postedBy")
                                                        .setValue(storyModel.getStoryAt())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                UserStories stories = new UserStories(uri.toString(),storyModel.getStoryAt());

                                                                database.getReference()
                                                                        .child("stories")
                                                                        .child(FirebaseAuth.getInstance().getUid())
                                                                        .child("userStories")
                                                                        .push()
                                                                        .setValue(stories);

                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });



                    }
                });




        return view;
    }
}