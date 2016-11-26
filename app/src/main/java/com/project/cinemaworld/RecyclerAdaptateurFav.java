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
public class RecyclerAdaptateurFav extends RecyclerView.Adapter<RecyclerAdaptateurFav.ServicesViewHolder> {

    private Context context;
    private ArrayList<ConstFilm> items;
    private ArrayList<ConstFilm> list_films=new ArrayList<ConstFilm>();
    private final OnItemLongClickListener listener;

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public RecyclerAdaptateurFav(Context context, ArrayList<ConstFilm> items,OnItemLongClickListener listener) {
        this.items = items;
        this.context = context;
        this.list_films=items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_card_fav, parent, false);
        ServicesViewHolder viewHolder = new ServicesViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ServicesViewHolder holder, final int position) {

        final ConstFilm salle = items.get(position);
        holder.nom_salle.setText(salle.getNom_film());

        // ImageLoader class instance
        Picasso.with(context).load(salle.getImage_film())
                //.transform(new BlurTransformation(this))
                //.transform(new ResizeTransformation(50))
                //.resize(50,50)
                //.fit().centerCrop()
                .error(R.color.color_primary)
                .fit().centerInside()
                .into(holder.ivFlag);
/////////////////////////////////////////////////////////////////////////////////////////////////////
         holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 /*String id= String.valueOf(salle.getId_favoris());
                 Toast.makeText(context,id,Toast.LENGTH_SHORT).show();*/
                 listener.onItemLongClicked(salle.getId_favoris());
                 return true;
             }
         });

/////////////////////////////////////////////////////////////////////////////////////////////////////

    }

        public class ServicesViewHolder extends RecyclerView.ViewHolder {

            private Context context;
            public TextView nom_salle;
            public ImageView ivFlag;



            public ServicesViewHolder(final Context context, final View itemView) {
                super(itemView);
                this.context = context;
                nom_salle = (TextView) itemView.findViewById(R.id.nom_fav);
                ivFlag = (ImageView) itemView.findViewById(R.id.image_fav);

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
