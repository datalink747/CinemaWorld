package com.project.cinemaworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Youtube_activity_view extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    public static final String API_KEY = "AIzaSyAkdPoYUxToG-1P3vGLR0KN0DBz88Pcgmo";
    public static final String VIDEO_ID = "rmzv716SYkQ";
    private String id_video,nom_film,gener_film,date_film,salle_film;
    private TextView y_salle_film,y_gener_film,y_date_film;
    private CollapsingToolbarLayout collapsingtoolbarlayout4;
    private LinearLayout layout;
    private FloatingActionButton floating_retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_activity_view);

        y_salle_film =(TextView)findViewById(R.id.f_nom_film);
        y_gener_film =(TextView)findViewById(R.id.f_gener_film);
        y_date_film =(TextView)findViewById(R.id.f_date_film);
        layout = (LinearLayout)findViewById(R.id.layout1);
        layout.setBackground(getDrawable(R.drawable.affiche_video));
        floating_retour = (FloatingActionButton)findViewById(R.id.floating_retour);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
        youTubePlayerView.initialize(getString(R.string.key_youtube), this);

        StrictMode.enableDefaults();
        Intent receivedIntent=getIntent();
        id_video=receivedIntent.getStringExtra("id_video");
        nom_film=receivedIntent.getStringExtra("nom_film");
        gener_film=receivedIntent.getStringExtra("gener_film");
        date_film=receivedIntent.getStringExtra("date_film");
        salle_film=receivedIntent.getStringExtra("salle_film");
        System.out.println("id_video =" + id_video);

        y_salle_film.setText(salle_film);
        y_gener_film.setText(gener_film);
        y_date_film.setText(date_film);

        collapsingtoolbarlayout4 =(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar4);
        collapsingtoolbarlayout4.setTitle(nom_film);

        floating_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Youtube_activity_view.this.finish();
            }
        });

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(id_video);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
