package com.project.cinemaworld;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Start_cinema extends AppCompatActivity {
    static private int SPLASH_TIME = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_cinema);

/**
    * func key sha1 using in facebook api.
 */
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash==>:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }



        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
            //Message = "connecter a Internet 3G ";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent mainIntent1 = new Intent(Start_cinema.this, Login_facebook.class);

                    startActivity(mainIntent1);
                    finish();
                }

            }, SPLASH_TIME);


        } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            //Message = "connecter a Internet WIFI ";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent mainIntent1 = new Intent(Start_cinema.this, Login_facebook.class);

                    startActivity(mainIntent1);
                    finish();
                }

            }, SPLASH_TIME);


        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("SVP Activer Internet !");
            alertDialogBuilder.setPositiveButton("Actualiser", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent mainIntent1 = new Intent(Start_cinema.this, Start_cinema.class);

                    startActivity(mainIntent1);
                    finish();

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }
}
