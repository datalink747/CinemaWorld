package com.project.cinemaworld;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.cinemaworld.Model.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Google_Maps_v2 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private ArrayList<ConstSallon> list_salle = new ArrayList<ConstSallon>();
    private static String TAG = FilmsFragment.class.getSimpleName();
    private MarkerOptions marker_item = new MarkerOptions();
    private GoogleMap map;
    private CoordinatorLayout coordinatorLayout;
    private boolean gps_enabled, wifi_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__maps_v2);



       /* MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);*/

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();

        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        wifi_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (wifi_enabled) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        }


        if (gps_enabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);


            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {

            } else if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {

            } else {
                Toast.makeText(Google_Maps_v2.this, "Pas de connection a internet", Toast.LENGTH_LONG).show();

            }


        } else

        {
            Toast.makeText(Google_Maps_v2.this,
                    "Impossible de détecter vos emplacement courant veuillez activer GPS....", Toast.LENGTH_LONG).show();


        }


        if (list_salle.isEmpty()) {
            makeJsonObjectRequest();
        } else {
            list_salle.clear();
            makeJsonObjectRequest();
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatormap);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                    final double p_latitude = marker.getPosition().latitude;
                    final double p_longitude = marker.getPosition().longitude;
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, marker.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction("Go", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    launchGoogleMaps(Google_Maps_v2.this, p_latitude, p_longitude, marker.getTitle());
                                }
                            });

                    snackbar.show();

                return true;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(35.857200, 10.595476);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        List<ConstSallon> contacts2 = list_salle;
        for (ConstSallon cn2 : contacts2) {
            LatLng pos_salon = new LatLng(cn2.getLattitude(), cn2.longitude);

            googleMap.addMarker(new MarkerOptions()
                    .title(cn2.getNom_salon())
                    .position(pos_salon));
        }
    }


    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://" + getString(R.string.ip_adresse) + EndPonts.url_salle, null, new Response.Listener<JSONObject>() {

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
                        item.setId_sallon(id_salle);
                        item.setNom_salon(nom_salle);
                        item.setLattitude(lattitude_salle);
                        item.setLongitude(longitude_salle);
                        item.setImage_salle("http://" + getString(R.string.ip_adresse) + EndPonts.url_affiche + image_salle);

                        list_salle.add(item);
                        Log.d("all menu 1: ", "> " + c);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  /*  Toast.makeText(MainActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }

                List<ConstSallon> contacts2 = list_salle;
                for (ConstSallon cn2 : contacts2) {
                    LatLng pos_salon = new LatLng(cn2.getLattitude(), cn2.getLongitude());

                    marker_item.position(new LatLng(cn2.getLattitude(), cn2.getLongitude()));
                    marker_item.title(cn2.getNom_salon())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_votre_salle));
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(marker_item);
                }


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

    public static void launchGoogleMaps(Context context, double latitude, double longitude, String label) {
        String format = "geo:0,0?q=" + Double.toString(latitude) + "," + Double.toString(longitude) + "(" + label + ")";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {

        if(location==null) {

            MarkerOptions mp = new MarkerOptions();
            mp.visible(true);
             System.out.println("latitude="+location.getLatitude());
             System.out.println("longitude="+location.getLongitude());
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mp.title("Ma position")
                     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            map.addMarker(mp);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 12));

        }
        else
        {
            float[] last_new_location = new float[3];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    location.getLatitude(), location.getLongitude(), last_new_location);
            double latnewdifference = (last_new_location[0] * 0.000621);

            if (latnewdifference > 0.1) {

                map.clear();

                List<ConstSallon> contacts2 = list_salle;
                for (ConstSallon cn2 : contacts2) {
                    LatLng pos_salon = new LatLng(cn2.getLattitude(), cn2.longitude);

                    marker_item.position(new LatLng(cn2.getLattitude(), cn2.getLongitude()));
                    marker_item.title(cn2.getNom_salon())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_votre_salle));
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(marker_item);
                }

            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        if ("gps".equals(provider)) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.removeUpdates(this);

        }

    }
}
