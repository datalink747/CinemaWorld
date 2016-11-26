package com.project.cinemaworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;*/

/**
 * Created by Soussi on 30/04/2016.
 */

public class Salon_filtre extends AppCompatActivity {

    private ArrayList<ConstFilm> list_films = new ArrayList<ConstFilm>();
    private RecyclerView mRecyclerView;
    private static String TAG = FilmsFragment.class.getSimpleName();
    private RecyclerView.Adapter mAdapter1;
    private String nom_salle;
    private Intent intent_recu;
    private ConstSallon const1;
    private JSONArray all_events = null;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_filtre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_filtre);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Salon_filtre.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        intent_recu = getIntent();
        const1 = (ConstSallon) intent_recu.getParcelableExtra("pos2");
        System.out.println("data recu :" + const1.getNom_salon());
        nom_salle = const1.getNom_salon();
        toolbar.setSubtitle(const1.getNom_salon());


        if (list_films.isEmpty()) {
            // makeJsonObjectRequest();
            new GetEvants().execute();
        } else {
            list_films.clear();
            // makeJsonObjectRequest();
            new GetEvants().execute();
        }


    }


    /**
     * Async task class to get json by making HTTP call
     * *//*
     */
    private class GetEvants extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nom_salle", nom_salle));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://" + getString(R.string.ip_adresse) + EndPonts.url_film_par_salle, ServiceHandler.GET, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                //  list_item_des.clear();
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    all_events = jsonObj.getJSONArray("list_films");
                    Log.d("all categories: ", "> " + all_events);

                    for (int i = 0; i < all_events.length(); i++) {
                        JSONObject c = all_events.getJSONObject(i);
                        int id_film = c.getInt("id_film");
                        String nom_film = c.getString("nom_film");
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
                        list_films.add(item);
                        Log.d("all menu 1: ", "> " + c);
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
            // Dismiss the progress dialog

            mAdapter1 = new RecyclerAdaptateurFilm(Salon_filtre.this, list_films);
            mRecyclerView.setAdapter(mAdapter1);

        }


    }//end asynctask


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_siteweb) {


            if (const1.getWebsite_salle().isEmpty()) {
                // put your logic to launch call app here
                Toast.makeText(Salon_filtre.this,"Site Web n'est pas Disponible !",Toast.LENGTH_SHORT).show();
            }
            else
            {
                final Intent webIntent = new Intent(Intent.ACTION_WEB_SEARCH, Uri.parse(const1.getWebsite_salle()));
                startActivity(webIntent);

            }

            return true;
        }
        if (id == R.id.action_tel) {


            if (const1.getTel_salle().isEmpty()) {
                // put your logic to launch call app here
                Toast.makeText(Salon_filtre.this,"N° Tél n'est pas Disponible !",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+const1.getTel_salle()));
                startActivity(dialIntent);
            }

            return true;
        }




        return super.onOptionsItemSelected(item);
    }

    }





