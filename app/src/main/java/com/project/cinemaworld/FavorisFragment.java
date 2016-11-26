package com.project.cinemaworld;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.project.cinemaworld.login_facebook.CircleTransform;
import com.project.cinemaworld.login_facebook.PrefUtils;
import com.project.cinemaworld.login_facebook.User;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Created by Soussi on 06/05/2016.
 */
public class FavorisFragment extends Fragment {
    private ImageView image_user,image_profil;
    private FloatingActionButton goto_favoris;
    private TextView name_user_f,email_user_f;
    private User user;
    private Bitmap bitmap,bitmapcover;
    private ProfilePictureView profilePictureView;
    private LinearLayout layout_fav;
    public FavorisFragment() {
        // Required empty public constructor
    }
    public static FavorisFragment newInstance() {
        return new FavorisFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoris, container, false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_profil =(ImageView)view.findViewById(R.id.image_profil);
        image_user =(ImageView)view.findViewById(R.id.image_users);
        goto_favoris =(FloatingActionButton)view.findViewById(R.id.gotofavori);
        email_user_f =(TextView)view.findViewById(R.id.email_user_f);
        name_user_f =(TextView)view.findViewById(R.id.name_user_f);
        layout_fav = (LinearLayout)view.findViewById(R.id.layout_fav);
        layout_fav.setBackground(getActivity().getDrawable(R.drawable.affiche_fav33));

        user= PrefUtils.getCurrentUser(getActivity());

        name_user_f.setText(user.facebookname);
        email_user_f.setText(user.email);
        Picasso.with(getActivity()).load("https://graph.facebook.com/" +user.facebookID + "/picture?type=large")
                .transform(new CircleTransform())
                .error(R.drawable.icon_acteur)
                .into(image_user);

      /*  Picasso.with(getActivity()).load("https://graph.facebook.com/" +user.facebookID + "?fields=cover&access_token=")
                .transform(new CircleTransform())
                .error(R.color.color_accent)
                .into(image_profil);*/

        goto_favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_favoris =new Intent(getActivity(),Listsfavoris.class);
                startActivity(goto_favoris);
            }
        });

        // fetching facebook's profile picture
        /*new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                URL imageURLcover = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" +user.facebookID + "/picture?type=large");
                    imageURLcover = new URL("https://graph.facebook.com/" +user.facebookID + "/fields=cover");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    bitmapcover  = BitmapFactory.decodeStream(imageURLcover.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
              //  image_user.setImageBitmap(bitmap);

                image_profil.setImageBitmap(bitmapcover);
            }
        }.execute();*/


    }

}
