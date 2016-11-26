package com.project.cinemaworld;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.cinemaworld.Model.AppController;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Suggestion extends Fragment {
    private EditText edit_sugg, edit_desc;
    private TextInputLayout input_layout_sugg, input_layout_desc;
    private Button btn_send_sugg;
    private Spinner sp_list_salle;
    private ArrayList<ConstSallon> list_salle=new ArrayList<ConstSallon>();
    private List<String> lables = new ArrayList<String>();
    private static String TAG = SallonsFragment.class.getSimpleName();
    private String des,sugg,sp_salle;
    private JSONArray all_events = null;

    public Suggestion() {
        // Required empty public constructor
    }


    public static Suggestion newInstance() {
        return new Suggestion();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input_layout_desc =(TextInputLayout)view.findViewById(R.id.input_layout_desc);
        input_layout_sugg =(TextInputLayout)view.findViewById(R.id.input_layout_sugg);
        edit_desc =(EditText)view.findViewById(R.id.edit_desc);
        edit_sugg =(EditText)view.findViewById(R.id.edit_sugg);
        btn_send_sugg =(Button)view.findViewById(R.id.btn_send_sugg);
        sp_list_salle =(Spinner)view.findViewById(R.id.list_salle);

        sp_list_salle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_salle =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn_send_sugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        if(list_salle.isEmpty())
        {
            makeJsonObjectRequest();
        }
        else {
            list_salle.clear();
            lables.clear();
            makeJsonObjectRequest();
        }

    }

    private void submitForm() {
        if (!validatesugg()) {
            return;
        }

        if (!validatedesc()) {
            return;
        }
      new GetEvants().execute();


    }

    private boolean validatesugg() {
        if (edit_sugg.getText().toString().trim().isEmpty()) {
            input_layout_sugg.setError("Champs vide !");
            requestFocus(edit_sugg);
            return false;
        } else {
            input_layout_sugg.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatedesc() {
        if (edit_desc.getText().toString().trim().isEmpty()) {
            input_layout_desc.setError("Champs vide !");
            requestFocus(edit_desc);
            return false;
        } else {
            input_layout_desc.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

                        ConstSallon item = new ConstSallon();

                        item.setNom_salon(nom_salle);


                        list_salle.add(item);
                        Log.d("all menu 1: ", "> " + c);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  /*  Toast.makeText(MainActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }


                for (int i = 0; i < list_salle.size(); i++) {
                    lables.add(list_salle.get(i).getNom_salon());
                }
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lables);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_list_salle.setAdapter(dataAdapter);


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

        private class GetEvants extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                des=edit_desc.getText().toString().trim();
                sugg=edit_sugg.getText().toString().trim();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                // Creating service handler class instance

                ServiceHandler sh = new ServiceHandler();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("des", des));
                params.add(new BasicNameValuePair("sugg", sugg));
                params.add(new BasicNameValuePair("sp_salle", sp_salle));
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall("http://"+getString(R.string.ip_adresse)+EndPonts.url_suggestion, ServiceHandler.POST,params);

                Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {

                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        all_events = jsonObj.getJSONArray("list_films");
                        Log.d("all suggestions: ", "> " + all_events);

                        for (int i = 0; i < all_events.length(); i++) {
                            JSONObject c = all_events.getJSONObject(i);



                            Log.d("all sugg 1: ", "> " + c);


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

                edit_desc.setText("");
                edit_sugg.setText("");

            }




        }
    }


