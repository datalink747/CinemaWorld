package com.project.cinemaworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Soussi on 29/04/2016.
 */
public class RecyclerAdaptateurFilm extends RecyclerView.Adapter<RecyclerAdaptateurFilm.ServicesViewHolder> {

    private Context context;
    private ArrayList<ConstFilm> items;
    private ArrayList<ConstFilm> list_films=new ArrayList<ConstFilm>();

    public RecyclerAdaptateurFilm(Context context, ArrayList<ConstFilm> items) {
        this.items = items;
        this.context = context;
        this.list_films=items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_card_film, parent, false);
        ServicesViewHolder viewHolder = new ServicesViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ServicesViewHolder holder, final int position) {

        ConstFilm films = items.get(position);
        holder.nom_film.setText(films.getNom_film());
        holder.date_film.setText(films.getNom_salle());//remplace par nom salle
        holder.genre_film.setText(films.getGenre_film());

        // ImageLoader class instance
        Picasso.with(context).load(films.getImage_film())
                //.transform(new BlurTransformation(this))
                //.transform(new ResizeTransformation(50))
                //.resize(50,50)
                //.fit().centerCrop()
                .error(R.drawable.logo3)
                .fit().centerInside()
                .into(holder.ivFlag);


    }

        public class ServicesViewHolder extends RecyclerView.ViewHolder {

            private Context context;
            public TextView nom_film, date_film, genre_film;
            public ImageView ivFlag;




            public ServicesViewHolder(final Context context, final View itemView) {
                super(itemView);
                this.context = context;
                nom_film = (TextView) itemView.findViewById(R.id.nom_film);
                date_film = (TextView) itemView.findViewById(R.id.date_film);
                genre_film = (TextView) itemView.findViewById(R.id.gener_film);
                ivFlag = (ImageView) itemView.findViewById(R.id.image_services);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent aff_detail =new Intent(context,Detail.class);
                        int position = getPosition();
                        aff_detail.putExtra("pos", items.get(position));
                        context.startActivity(aff_detail);
                    }
                });

            }

        }





}
