package com.project.cinemaworld;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.cinemaworld.login_facebook.CircleTransform;
import com.project.cinemaworld.login_facebook.PrefUtils;
import com.project.cinemaworld.login_facebook.User;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listsfavoris extends AppCompatActivity {
    private User user;
    private TextView name_user_fav,email_user_fav;
    private ImageView image_user_fav;
    private FloatingActionButton actualisation_fav;
    private ArrayList<ConstFilm> list_favoris=new ArrayList<ConstFilm>();
    private RecyclerView mRecyclerView_fav;
    private static String TAG = Listsfavoris.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;
    private JSONArray all_fav = null;
    private CollapsingToolbarLayout collapsingtoolbarlayout;
    private LinearLayout layout_fav;
    private String pos_fav;//get position from adapter
    private String msg;//show result in toast from json object

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listsfavoris);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user= PrefUtils.getCurrentUser(Listsfavoris.this);

        email_user_fav =(TextView)findViewById(R.id.email_user_fav);
        name_user_fav =(TextView)findViewById(R.id.name_user_fav);
        image_user_fav =(ImageView)findViewById(R.id.image_users_fav);
        mRecyclerView_fav = (RecyclerView)findViewById(R.id.recyclerView_fav);
        actualisation_fav =(FloatingActionButton)findViewById(R.id.btn_actualisation);

        collapsingtoolbarlayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_fav);
        layout_fav =(LinearLayout)findViewById(R.id.layout_fav);
        layout_fav.setBackground(getDrawable(R.drawable.affiche_fav33));




        name_user_fav.setText(user.facebookname);
        email_user_fav.setText(user.email);
        Picasso.with(Listsfavoris.this).load("https://graph.facebook.com/" +user.facebookID + "/picture?type=large")
                .transform(new CircleTransform())
                .error(R.drawable.icon_acteur)
                .into(image_user_fav);

        //btn actualisation
        actualisation_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list_favoris.isEmpty())
                {
                 new GetMesFavoris().execute();
                }
                else {
                    list_favoris.clear();
                    new GetMesFavoris().execute();
                }

            }
        });

        mRecyclerView_fav.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));



        if(list_favoris.isEmpty())
        {
            new GetMesFavoris().execute();
        }
        else {
            list_favoris.clear();
            new GetMesFavoris().execute();
        }

    }

    /**
     * Async task class to get json by making HTTP call
     * *//*
     */
    private class GetMesFavoris extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_user", user.facebookID));


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://" + getString(R.string.ip_adresse) + EndPonts.url_get_favoris, ServiceHandler.GET, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    all_fav = jsonObj.getJSONArray("list_favoris");
                    Log.d("all favoris: ", "> " + all_fav);

                    for (int i = 0; i < all_fav.length(); i++) {
                        JSONObject c = all_fav.getJSONObject(i);
                        int id_film = c.getInt("id_films");
                        int id_favoris = c.getInt("id_favoris");
                        String nom_film = c.getString("nom_films");
                        String synopsis_film = c.getString("synopsis_film");
                        String date_film = c.getString("date_film");
                        String realisateurs_film = c.getString("realisateurs_film");
                        String acteurs_film = c.getString("acteurs_film");
                        String genre_film = c.getString("genre_film");
                       String nationalite_film = c.getString("nationalite_film");
                        String bande_film = c.getString("bande_film");
                        String image_film = c.getString("image_film");
                        String salle_film = c.getString("nom_salle");
                        String horaires_film = c.getString("horaires");
                        String duree_film = c.getString("duree_film");
                       String prix_film = c.getString("prix_film");

                        ConstFilm item = new ConstFilm();
                        item.setId_film(id_film);
                        item.setId_favoris(id_favoris);
                        item.setNom_film(nom_film);
                        item.setSynopsis_film(synopsis_film);
                        item.setDate_sortie_film(date_film);
                        item.setRealisateurs_film(realisateurs_film);
                       item.setActeur_film(acteurs_film);
                       item.setGenre_film(genre_film);
                        item.setNationalite_film(nationalite_film);
                        item.setBonde_annonce_film(bande_film);
                        item.setHoraires(horaires_film);
                        item.setDuree_film(duree_film);
                        item.setPrix_film(prix_film);
                        item.setImage_film("http://" + getString(R.string.ip_adresse) + EndPonts.url_affiche + image_film);
                        item.setNom_salle(salle_film);
                        list_favoris.add(item);
                        Log.d("all json favoris 1: ", "> " + c);
                        Log.d("image film: ", "> " + "http://" + getString(R.string.ip_adresse) + EndPonts.url_affiche + image_film);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            mAdapter = new RecyclerAdaptateurFav(Listsfavoris.this, list_favoris, new RecyclerAdaptateurFav.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClicked(int position) {
                  //  String pos= String.valueOf(position);
                    pos_fav= String.valueOf(position);
                    showconfirmeDialog();//confirmation la suppr
                  //  Toast.makeText(Listsfavoris.this,pos,Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mRecyclerView_fav.setAdapter(mAdapter);
            collapsingtoolbarlayout.setTitle("Mes Favoris ("+mAdapter.getItemCount()+")");

        }


    }//end asynctask

    @Override
    protected void onResume() {
        super.onResume();
        user= PrefUtils.getCurrentUser(Listsfavoris.this);
    }

    @Override
    protected void onPause() {
        user= PrefUtils.getCurrentUser(Listsfavoris.this);
        super.onPause();
    }

    private class sup_favoris extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            ServiceHandler sh = new ServiceHandler();
           // String pos_favoris = String.valueOf(pos_fav);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("favid", pos_fav));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://" + getString(R.string.ip_adresse) + EndPonts.url_sup_favoris, ServiceHandler.GET, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {


                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    msg= jsonObj.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(list_favoris.isEmpty())
            {
                new GetMesFavoris().execute();
            }
            else {
                list_favoris.clear();
                new GetMesFavoris().execute();
            }
            Toast.makeText(Listsfavoris.this,msg,Toast.LENGTH_SHORT).show();

        }
    }

    private void showconfirmeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listsfavoris.this);
        builder.setTitle(getString(R.string.name_titre));
        builder.setMessage("Voulez vous supprimer cette favoris !");
        builder.setIcon(R.drawable.icon_start);
        String positiveText = "Oui";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        new sup_favoris().execute();
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






}
