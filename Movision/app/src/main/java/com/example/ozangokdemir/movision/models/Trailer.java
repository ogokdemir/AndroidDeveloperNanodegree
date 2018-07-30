package com.example.ozangokdemir.movision.models;


import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

@Parcel
public class Trailer {

    @SerializedName("key")
    String mKey;
    @SerializedName("site")
    String mSite;
    @SerializedName("type")
    String mType;
    @SerializedName("name")
    String mName;


    public String getKey() {
        return mKey;
    }

    public String getSite() {
        return mSite;
    }

    public String getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }
}
