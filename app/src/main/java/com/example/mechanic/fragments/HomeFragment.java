package com.example.mechanic.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mechanic.PendingComplaintsActivity;
import com.example.mechanic.R;
import com.example.mechanic.Requests;
import com.example.mechanic.ScanQRActivity;
import com.example.mechanic.adapters.ViewPagerAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    CardView scan;
    CardView complaints;
    CardView request;
    ViewPager viewPager;
    LinearLayout sliderdotspanel;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.home_fragment, container, false);

        viewPager= (ViewPager)view.findViewById(R.id.viewpager);

        ViewPagerAdapter sviewPagerAdapter = new ViewPagerAdapter(getActivity().getApplicationContext());
        viewPager.setAdapter(sviewPagerAdapter);



        //dots in viewpager
        sliderdotspanel = (LinearLayout) view.findViewById(R.id.slider_dots);

        dotscount=sviewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getActivity().getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderdotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //timer in viewpager
        autoScroll();

        scan = view.findViewById(R.id.scan);
        complaints = view.findViewById(R.id.complaints);
        request = view.findViewById(R.id.requests);



        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });


        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), PendingComplaintsActivity.class);
                startActivity(i);


            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Requests.class);
                startActivity(intent);

            }
        });

        return view;
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity().getApplicationContext(), ScanQRActivity.class);
        startActivity(intent);

    }


    final long DELAY = 1000;//delay in milliseconds before auto sliding starts.
    final long PERIOD = 4000; //time in milliseconds between sliding.


    private void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if(viewPager.getCurrentItem()==0)
                {
                    viewPager.setCurrentItem(1);
                }
                else if(viewPager.getCurrentItem()==1)
                {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        timer = new Timer(); // creating a new thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY, PERIOD);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}