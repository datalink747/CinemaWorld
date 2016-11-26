package com.project.cinemaworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Maps_Activity_v2 extends Fragment  {

    private FloatingActionButton gotomap;

    public Maps_Activity_v2() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        return new Maps_Activity_v2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_maps__activity_v2, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

gotomap =(FloatingActionButton)view.findViewById(R.id.gotomap);
        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startmap =new Intent(getActivity(),Google_Maps_v2.class);
                startActivity(startmap);
            }
        });


    }





}
