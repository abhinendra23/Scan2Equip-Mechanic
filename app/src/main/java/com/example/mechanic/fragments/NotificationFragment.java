package com.example.mechanic.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic.DatabaseHelper;
import com.example.mechanic.R;
import com.example.mechanic.adapters.NotificationAdapter;
import com.example.mechanic.model.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    ScrollView scrollView;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    LinearLayoutManager VerticalLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notification_fragment, container, false);



        user = FirebaseAuth.getInstance().getCurrentUser();

//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_notfication_tb);


        scrollView = view.findViewById(R.id.empty_scrolView);

        recyclerView = view.findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(VerticalLayout);




        DatabaseHelper mydb = new DatabaseHelper(getActivity(),user.getUid());
        Cursor res = mydb.getAllData(user.getUid());

        if(res.getCount()==0)
        {
            scrollView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            ArrayList<NotificationModel> example = new ArrayList<NotificationModel>();

            while(res.moveToNext()) {
                example.add(new NotificationModel(res.getString(1), res.getString(2), res.getString(3)));
            }

            NotificationAdapter adapter = new NotificationAdapter(example,this.getActivity().getApplicationContext());
            recyclerView.setAdapter(adapter);
        }



        return view;
    }
}
