package com.project.cinemaworld;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SallonsFragment extends Fragment {

    private ArrayList<ConstSallon> list_salle=new ArrayList<ConstSallon>();
    private RecyclerView mRecyclerView1;
    private static String TAG = SallonsFragment.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;

    public SallonsFragment() {
        // Required empty public constructor
    }

    public static SallonsFragment newInstance() {
        return new SallonsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sallons, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView_salle);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
       // mRecyclerView1.setLayoutManager(layoutManager);
       // mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));



        if(list_salle.isEmpty())
        {
            makeJsonObjectRequest();
        }
        else {
            list_salle.clear();
            makeJsonObjectRequest();
        }
    }


    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://"+getString(R.string.ip_adresse)+EndPonts.url_salle, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String success = response.getString("success");
                    String Date = response.getString("Date");

                    JSONArray service_clients = response.getJSONArray("list_salles");
                    for (int i = 0; i < service_clients.length(); i++) {
                        JSONObject c = service_clients.getJSONObject(i);
                        int id_salle = c.getInt("id_salle");
                        String nom_salle = c.getString("nom_salle");
                        Double longitude_salle = c.getDouble("longitude_salle");
                        Double lattitude_salle = c.getDouble("lattitude_salle");
                        String image_salle = c.getString("image_salle");
                        String tel_salle = c.getString("tel_salle");
                        String siteweb_salle = c.getString("site_web_salle");

                        ConstSallon item = new ConstSallon();
                        item.setId_sallon(id_salle);
                        item.setNom_salon(nom_salle);
                        item.setLattitude(lattitude_salle);
                        item.setLongitude(longitude_salle);
                        item.setTel_salle(tel_salle);
                        item.setWebsite_salle(siteweb_salle);
                        item.setImage_salle("http://"+getString(R.string.ip_adresse)+EndPonts.url_affiche+image_salle);

                        list_salle.add(item);
                        Log.d("all salle 1: ", "> " + c);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  /*  Toast.makeText(MainActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                    Log.d("Erreur all salle 1: ", "> " + e.getMessage());
                }

                mAdapter = new RecyclerAdaptateurSalle(getActivity(), list_salle);
                mRecyclerView1.setAdapter(mAdapter);


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

}
