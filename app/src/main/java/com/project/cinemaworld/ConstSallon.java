package com.project.cinemaworld;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Soussi on 29/04/2016.
 */
public class ConstSallon implements Parcelable {

    int id_sallon;
    String nom_salon;
    Double longitude;
    Double lattitude;
    String image_salle;
    String tel_salle;
    String website_salle;

    public ConstSallon() {
    }

    protected ConstSallon(Parcel in) {
        id_sallon = in.readInt();
        nom_salon = in.readString();
        image_salle = in.readString();
        longitude = in.readDouble();
        lattitude = in.readDouble();
        tel_salle = in.readString();
        website_salle = in.readString();
    }

    public static final Creator<ConstSallon> CREATOR = new Creator<ConstSallon>() {
        @Override
        public ConstSallon createFromParcel(Parcel in) {
            return new ConstSallon(in);
        }

        @Override
        public ConstSallon[] newArray(int size) {
            return new ConstSallon[size];
        }
    };

    public int getId_sallon() {
        return id_sallon;
    }

    public void setId_sallon(int id_sallon) {
        this.id_sallon = id_sallon;
    }

    public String getNom_salon() {
        return nom_salon;
    }

    public void setNom_salon(String nom_salon) {
        this.nom_salon = nom_salon;
    }

    public String getTel_salle() {
        return tel_salle;
    }

    public void setTel_salle(String tel_salle) {
        this.tel_salle = tel_salle;
    }

    public String getWebsite_salle() {
        return website_salle;
    }

    public void setWebsite_salle(String website_salle) {
        this.website_salle = website_salle;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public String getImage_salle() {
        return image_salle;
    }

    public void setImage_salle(String image_salle) {
        this.image_salle = image_salle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_sallon);
        dest.writeString(nom_salon);
        dest.writeString(image_salle);
        dest.writeDouble(lattitude);
        dest.writeDouble(longitude);
        dest.writeString(tel_salle);
        dest.writeString(website_salle);
    }

}
