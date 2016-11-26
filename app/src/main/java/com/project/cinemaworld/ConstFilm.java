package com.project.cinemaworld;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Soussi on 29/04/2016.
 */
public class ConstFilm implements Parcelable {
    int id_film,id_favoris;
    String nom_film;
    String synopsis_film;
    String date_sortie_film;
    String realisateurs_film;
    String acteur_film;
    String genre_film;
    String nationalite_film;
    String bonde_annonce_film;
    String image_film;
    String horaires;
    String duree_film;
    String prix_film;

    public ConstFilm() {
    }

    protected ConstFilm(Parcel in) {
        id_film = in.readInt();
        nom_film = in.readString();
        synopsis_film = in.readString();
        date_sortie_film = in.readString();
        realisateurs_film = in.readString();
        acteur_film = in.readString();
        genre_film = in.readString();
        nationalite_film = in.readString();
        bonde_annonce_film = in.readString();
        image_film = in.readString();
        nom_salle = in.readString();
        horaires = in.readString();
        duree_film = in.readString();
        prix_film = in.readString();
        id_favoris = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_film);
        dest.writeString(nom_film);
        dest.writeString(synopsis_film);
        dest.writeString(date_sortie_film);
        dest.writeString(realisateurs_film);
        dest.writeString(acteur_film);
        dest.writeString(genre_film);
        dest.writeString(nationalite_film);
        dest.writeString(bonde_annonce_film);
        dest.writeString(image_film);
        dest.writeString(nom_salle);
        dest.writeString(horaires);
        dest.writeString(duree_film);
        dest.writeString(prix_film);
        dest.writeInt(id_favoris);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConstFilm> CREATOR = new Creator<ConstFilm>() {
        @Override
        public ConstFilm createFromParcel(Parcel in) {
            return new ConstFilm(in);
        }

        @Override
        public ConstFilm[] newArray(int size) {
            return new ConstFilm[size];
        }
    };

    public String getNom_salle() {
        return nom_salle;
    }

    public void setNom_salle(String nom_salle) {
        this.nom_salle = nom_salle;
    }

    String nom_salle;

    public int getId_film() {
        return id_film;
    }

    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    public String getNom_film() {
        return nom_film;
    }

    public void setNom_film(String nom_film) {
        this.nom_film = nom_film;
    }

    public String getSynopsis_film() {
        return synopsis_film;
    }

    public void setSynopsis_film(String synopsis_film) {
        this.synopsis_film = synopsis_film;
    }

    public String getDate_sortie_film() {
        return date_sortie_film;
    }

    public void setDate_sortie_film(String date_sortie_film) {
        this.date_sortie_film = date_sortie_film;
    }

    public String getRealisateurs_film() {
        return realisateurs_film;
    }

    public void setRealisateurs_film(String realisateurs_film) {
        this.realisateurs_film = realisateurs_film;
    }

    public int getId_favoris() {
        return id_favoris;
    }

    public void setId_favoris(int id_favoris) {
        this.id_favoris = id_favoris;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public String getDuree_film() {
        return duree_film;
    }

    public void setDuree_film(String duree_film) {
        this.duree_film = duree_film;
    }

    public String getPrix_film() {
        return prix_film;
    }

    public void setPrix_film(String prix_film) {
        this.prix_film = prix_film;
    }

    public String getActeur_film() {
        return acteur_film;
    }

    public void setActeur_film(String acteur_film) {
        this.acteur_film = acteur_film;
    }

    public String getGenre_film() {
        return genre_film;
    }

    public void setGenre_film(String genre_film) {
        this.genre_film = genre_film;
    }

    public String getNationalite_film() {
        return nationalite_film;
    }

    public void setNationalite_film(String nationalite_film) {
        this.nationalite_film = nationalite_film;
    }

    public String getBonde_annonce_film() {
        return bonde_annonce_film;
    }

    public void setBonde_annonce_film(String bonde_annonce_film) {
        this.bonde_annonce_film = bonde_annonce_film;
    }

    public String getImage_film() {
        return image_film;
    }

    public void setImage_film(String image_film) {
        this.image_film = image_film;
    }
}

