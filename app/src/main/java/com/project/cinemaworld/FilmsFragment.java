package com.project.cinemaworld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.cinemaworld.Model.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FilmsFragment extends Fragment {

    private ArrayList<ConstFilm> list_films=new ArrayList<ConstFilm>();
    private RecyclerView mRecyclerView;
    private static String TAG = FilmsFragment.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;

    public static FilmsFragment newInstance() {
        return new FilmsFragment();
    }

    public FilmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_films, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

       // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);




        if(list_films.isEmpty())
        {
            makeJsonObjectRequest();

        }
        else {
            list_films.clear();
            makeJsonObjectRequest();

        }
    }


     /*
    * get database from json
    * */

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,"http://"+getString(R.string.ip_adresse)+EndPonts.url_film, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String success = response.getString("success");
                    String Date = response.getString("Date");

                    JSONArray service_clients = response.getJSONArray("list_films");
                    for (int i = 0; i < service_clients.length(); i++) {
                        JSONObject c = service_clients.getJSONObject(i);
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
                        item.setImage_film("http://"+getString(R.string.ip_adresse)+EndPonts.url_affiche+image_film);
                        item.setNom_salle(salle_film);
                        list_films.add(item);
                        Log.d("all menu 1: ", "> " + c);
                        Log.d("image film: ", "> " + "http://"+getString(R.string.ip_adresse)+EndPonts.url_affiche+image_film);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  /*  Toast.makeText(MainActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }

                     mAdapter = new RecyclerAdaptateurFilm(getActivity(), list_films);
                     mRecyclerView.setAdapter(mAdapter);
                   //  mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                /*Toast.makeText(MainActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                // hide the progress dialog
                //hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /*
    * */
}








