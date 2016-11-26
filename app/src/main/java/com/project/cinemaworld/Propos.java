package com.project.cinemaworld;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class Propos extends AppCompatActivity {
    private ImageView aff_propos;
    private CollapsingToolbarLayout collapsingtoolbarlayout2;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aff_propos=(ImageView)findViewById(R.id.backgroundaff);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_mail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent();
                mailIntent.setAction(Intent.ACTION_SEND);
                mailIntent.setType("message/rfc822");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "contact");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(mailIntent, EndPonts.email_to));
            }
        });

        aff_propos.setImageDrawable(getDrawable(R.drawable.them));
        collapsingtoolbarlayout2 =(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar2);
        collapsingtoolbarlayout2.setTitle(getString(R.string.name_titre));
    }

    @Override
    public void onBackPressed() {
        Propos.this.finish();
    }
}
