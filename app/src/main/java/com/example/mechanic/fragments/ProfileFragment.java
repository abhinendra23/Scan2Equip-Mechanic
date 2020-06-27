package com.example.mechanic.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.CircleTransform;
import com.example.mechanic.R;
import com.example.mechanic.SettingActivity;
import com.example.mechanic.adapters.ReviewAdapter;
import com.example.mechanic.dialogBox.CustomDialogBox;
import com.example.mechanic.model.Machine;
import com.example.mechanic.model.MechRating;
import com.example.mechanic.model.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    ImageView profilePicChange, profilePic;
    TextView s_rating;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser user;
    UploadTask uploadTask;
    CustomDialogBox dialogBox;

    TextView name,email,phoneNumber;


    RecyclerView recyclerView_machine;
    ReviewAdapter reviewAdapter;

    Button editButton;
    ConstraintLayout profileLayout, profileEditLayout;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    LinearLayoutManager HorizontalLayout;

    EditText nameEt,emailEt;
    Button save,cancel;

    private Toolbar mTopToolbar;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mTopToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mTopToolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        profilePicChange = view.findViewById(R.id.s_change_profile);
        profilePic = view.findViewById(R.id.profilepic);
        s_rating = view.findViewById(R.id.s_rating);
        editButton = view.findViewById(R.id.edit_button);
        profileLayout = view.findViewById(R.id.profile_layout);
        profileEditLayout = view.findViewById(R.id.profile_edit_layout);
        nameEt = view.findViewById(R.id.edit_profile_name);
        emailEt = view.findViewById(R.id.edit_profile_email);
        save = view.findViewById(R.id.save_edit_profile);
        cancel = view.findViewById(R.id.cancel_edit_profile);

//        int distance = 8;
//        float scale = getActivity().getResources().getDisplayMetrics().density;
//        view.setCameraDistance(distance * scale);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity().getApplicationContext();
                AnimatorSet flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_out);
                AnimatorSet flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_in);
                flipIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        profileEditLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        profileLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                flipOut.setTarget(profileLayout);
                flipIn.setTarget(profileEditLayout);
                flipOut.start();
                flipIn.start();


            }
        });

        dialogBox = new CustomDialogBox(getActivity());
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.show();

        user = FirebaseAuth.getInstance().getCurrentUser();

        name = view.findViewById(R.id.sm_profile_name);
        email = view.findViewById(R.id.sm_profile_email);
        phoneNumber = view.findViewById(R.id.sm_profile_phone);
//
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Mechanic")
                .child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Mechanic mechanic = dataSnapshot.getValue(Mechanic.class);

                if(dataSnapshot.hasChild("profilePicLink"))
                    Picasso.get().load(mechanic.getProfilePicLink()).into(profilePic);

                name.setText(mechanic.getUserName());
                email.setText(mechanic.getEmail());
                s_rating.setText(Float.toString(mechanic.getOverallRating()) );
                s_rating.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star,0,0,0);


                dialogBox.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid()+".jpeg");

        profilePicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Activity activity = getActivity();
                if (activity != null)
                    startActivityForResult(i, 12);
            }
        });


        //Horizontal recycler view
        recyclerView_machine=view.findViewById(R.id.horizontal_rv);
        recyclerView_machine.setLayoutManager(new LinearLayoutManager(getActivity()));
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView_machine.setLayoutManager(HorizontalLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic").child(user.getUid()).child("Ratings");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<MechRating> options = new DatabasePagingOptions.Builder<MechRating>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,MechRating.class)
                .build();

        reviewAdapter = new ReviewAdapter(options,getActivity().getApplicationContext());
        recyclerView_machine.setAdapter(reviewAdapter);
        reviewAdapter.startListening();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity().getApplicationContext();
                AnimatorSet flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_out);
                AnimatorSet flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_in);
                flipIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        profileLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        profileEditLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                flipOut.setTarget(profileEditLayout);
                flipIn.setTarget(profileLayout);
                flipOut.start();
                flipIn.start();


            }
        });
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent= new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("PRofile", "helo");
        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("PRofile", "helo1");

            dialogBox.show();

            Uri imageUri = data.getData();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            profilePic.setDrawingCacheEnabled(true);
            profilePic.buildDrawingCache();

            Picasso.get().load(imageUri).transform(new CircleTransform()).into(profilePic, new Callback() {
                @Override
                public void onSuccess() {

                    ((BitmapDrawable) profilePic.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);

                    Log.i("hello ankit", "ankit");
                    final byte[] image_data = baos.toByteArray();
                    uploadTask = storageReference.putBytes(image_data);

                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogBox.dismiss();
                            uploadTask.cancel();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageLink = uri.toString();

                                    HashMap<String, Object> updateProfilePic = new HashMap<>();

                                    updateProfilePic.put("/Users/Mechanic/" + user.getUid() + "/profilePicLink", imageLink);

                                    FirebaseDatabase.getInstance().getReference().updateChildren(updateProfilePic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogBox.dismiss();
                                        }
                                    });


                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }






}
