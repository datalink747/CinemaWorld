package com.project.cinemaworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.project.cinemaworld.login_facebook.PrefUtils;
import com.project.cinemaworld.login_facebook.User;
import com.project.cinemaworld.notification.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private User user;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user= PrefUtils.getCurrentUser(MainActivity.this);

        /**
            * func notification.
         */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(EndPonts.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                   // Toast.makeText(MainActivity.this,"ok notification",Toast.LENGTH_SHORT).show();
                    Log.d("notification: ", "> " + "ok notification");
                } else {
                   // Toast.makeText(MainActivity.this,"erreur notification",Toast.LENGTH_SHORT).show();
                    Log.d("notification: ", "> " + "erreur notification");
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        /**
            * end notification.
         */

        setupWindowAnimations(); // func for animation here

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        

        //on remplit notre viewpager

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                Fragment f = null;
                switch (position) {
                    case 0:
                        f = FilmsFragment.newInstance();
                        break;
                    case 1:
                        f = Maps_Activity_v2.newInstance();
                        break;
                    case 2:
                        f = SallonsFragment.newInstance();
                        break;
                    case 3:
                        f = FavorisFragment.newInstance();
                        break;
                    case 4:
                        f = Suggestion.newInstance();

                }

                return f;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                String tab_name = "";

                switch (position) {
                    case 0:
                        tab_name = "Films";
                        break;
                    case 1:
                        tab_name = "Maps";
                        break;
                    case 2:
                        tab_name = "Salons";
                        break;
                    case 3:
                        tab_name = "Favoris";
                        break;
                    case 4:
                        tab_name = "Suggestion";
                        break;

                }
                return tab_name;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });


//indique au tablayout quel est le viewpager à écouter
        tabLayout.setupWithViewPager(viewPager);



    }

    //animation for window
    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Explode explode = new Explode();
            getWindow().setExitTransition(explode);

            Fade fade = new Fade();
            getWindow().setReenterTransition(fade);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent go_propos =new Intent(MainActivity.this,Propos.class);
            startActivity(go_propos);
            return true;
        }

        if (id == R.id.action_exit) {
            showLocationDialog();
            return true;
        }

        if (id == R.id.action_logout_facebook) {
            PrefUtils.clearCurrentUser(MainActivity.this);
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.name_titre));
        builder.setMessage("êtes vous sûr de vouloir Quitter !");

        String positiveText = "Oui";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        MainActivity.this.finish();
                    }
                });

        String negativeText = "Non";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(EndPonts.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Vérifier si notre utilisateur a l'application Google Play Service
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
