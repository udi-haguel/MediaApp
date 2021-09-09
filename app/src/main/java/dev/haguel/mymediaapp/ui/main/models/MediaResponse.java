package dev.haguel.mymediaapp.ui.main.models;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MediaResponse {

    @SerializedName("results")
    private ArrayList<Media> mediaList;

    public MediaResponse() {}

    public ArrayList<Media> getMediaList() { return mediaList; }

    public void setMediaList(ArrayList<Media> mediaList) { this.mediaList = mediaList; }

    @NonNull
    @Override
    public String toString() {
        return "MediaResponse{" +
                "mediaList=" + mediaList +
                '}';
    }
}
