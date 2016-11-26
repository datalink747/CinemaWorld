package com.project.cinemaworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.project.cinemaworld.login_facebook.PrefUtils;
import com.project.cinemaworld.login_facebook.User;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Detail extends AppCompatActivity {
    private Intent intent_recu;
    private ConstFilm const1;
    private TextView synapsis,date_sortie,realisateur,acteurs,gener,nationalite,salle_film,prix,horaire,duree;
    private CollapsingToolbarLayout collapsingtoolbarlayout;
    private ImageView affichafe;
    private AppCompatButton play;
    private FloatingActionButton partage,add_favoris;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private Bitmap image_btm;
    private User user;
    private String id_film,msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_events);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        user= PrefUtils.getCurrentUser(Detail.this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(R.drawable.retour2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail.this.finish();
            }
        });



        intent_recu=getIntent();
        const1= (ConstFilm) intent_recu.getParcelableExtra("pos");
        System.out.println("data recu :" + const1.getNom_film());
        id_film= String.valueOf(const1.getId_film());

        salle_film = (TextView)findViewById(R.id.salle_film);
        synapsis = (TextView)findViewById(R.id.synapsis_film);
        date_sortie = (TextView)findViewById(R.id.date_film);
        realisateur = (TextView)findViewById(R.id.realisateur_film);
        acteurs = (TextView)findViewById(R.id.acteur_film);
        gener = (TextView)findViewById(R.id.gener_film);
        nationalite = (TextView)findViewById(R.id.nationalite_film);
        horaire = (TextView)findViewById(R.id.horaire_film);
        duree = (TextView)findViewById(R.id.duree_film);
        prix = (TextView)findViewById(R.id.prix_film);
        affichafe = (ImageView) findViewById(R.id.backgroundImageView);
        play = (AppCompatButton) findViewById(R.id.play_video);
        partage =(FloatingActionButton)findViewById(R.id.partage);
        add_favoris =(FloatingActionButton)findViewById(R.id.fab_favoris);

        salle_film.setText(const1.getNom_salle());
        synapsis.setText(const1.getSynopsis_film());

        makeTextViewResizable(synapsis, 2, "Afficher Plus", true);

        date_sortie.setText(const1.getDate_sortie_film());
        realisateur.setText(const1.getRealisateurs_film());
        acteurs.setText(const1.getActeur_film());
        gener.setText(const1.getGenre_film());
        nationalite.setText(const1.getNationalite_film());
        horaire.setText(const1.getHoraires());
        duree.setText(const1.getDuree_film());
        prix.setText(const1.getPrix_film());

        Picasso.with(Detail.this).load(const1.getImage_film())

                .error(R.drawable.logo3)
                .fit().centerInside()
                .into(affichafe);

        collapsingtoolbarlayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingtoolbarlayout.setTitle(const1.getNom_film());

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(! const1.getBonde_annonce_film().isEmpty()) {
                    Intent gotovideo = new Intent(Detail.this, Youtube_activity_view.class);
                    gotovideo.putExtra("id_video", const1.getBonde_annonce_film());
                    gotovideo.putExtra("salle_film", const1.getNom_salle());
                    gotovideo.putExtra("nom_film", const1.getNom_film());
                    gotovideo.putExtra("gener_film", const1.getGenre_film());
                    gotovideo.putExtra("date_film", const1.getDate_sortie_film());
                    startActivity(gotovideo);
                }
                else
                {

                    Snackbar.make(v, "Video non disponible !", Snackbar.LENGTH_LONG)

                            .setAction("Retour", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();


                }

            }
        });

        // partage contents to facebook
        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbackManager = CallbackManager.Factory.create();

                List<String> permissionNeeds = Arrays.asList("publish_actions");

                //this loginManager helps you eliminate adding a LoginButton to your UI
                loginManager = LoginManager.getInstance();

                loginManager.logInWithPublishPermissions(Detail.this, permissionNeeds);

                loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////
                     new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;

                try {
                    imageURL = new URL(const1.getImage_film());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    image_btm  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);



                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image_btm)
                        .setCaption(const1.getNom_film()+" #"+const1.getNom_salle().toString().trim())
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()

                        .addPhoto(photo)
                        .build();

                ShareApi.share(content, null);

            }
        }.execute(); // end AsyncTask

                    } //end onSuccess

                    @Override
                    public void onCancel()
                    {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {
                        System.out.println("onError");
                    }
                });




            }
        });//end fab_partage

        // btn_add favoris
        add_favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            new add_favoris().execute();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    // Ajoutez à chaque activité de longue durée
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    // pour Android, vous devez également enregistrer la désactivation de l’app
    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }


    private class add_favoris extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_facebook", user.facebookID));
            params.add(new BasicNameValuePair("email_facebook", user.email));
            params.add(new BasicNameValuePair("id_film", id_film.toString()));
            params.add(new BasicNameValuePair("nom_film", const1.getNom_film()));
            params.add(new BasicNameValuePair("nom_salle", const1.getNom_salle()));
            params.add(new BasicNameValuePair("date_film", const1.getDate_sortie_film()));
            params.add(new BasicNameValuePair("genre_film", const1.getGenre_film()));
            params.add(new BasicNameValuePair("acteurs_film", const1.getActeur_film()));
            params.add(new BasicNameValuePair("bande_film", const1.getBonde_annonce_film()));
            params.add(new BasicNameValuePair("image_film", const1.getImage_film().substring(33)));
            params.add(new BasicNameValuePair("nationalite_film", const1.getNationalite_film()));
            params.add(new BasicNameValuePair("realisateurs_film", const1.getRealisateurs_film()));
            params.add(new BasicNameValuePair("synopsis_film", const1.getSynopsis_film()));
            params.add(new BasicNameValuePair("duree_film", const1.getDuree_film()));
            params.add(new BasicNameValuePair("horaires", const1.getHoraires()));
            params.add(new BasicNameValuePair("prix_film", const1.getPrix_film()));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://" + getString(R.string.ip_adresse) + EndPonts.url_add_favoris, ServiceHandler.POST, params);

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

            Toast.makeText(Detail.this,msg,Toast.LENGTH_SHORT).show();

        }
    }


    //textview show more

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Retour", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "Afficher Plus", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }




    }
