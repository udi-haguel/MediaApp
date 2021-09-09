package dev.haguel.mymediaapp.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.MediaApiManager;

public class SharedViewModel extends ViewModel {

    MediaApiManager mediaApiManager;

    MutableLiveData<List<Media>> mediaListLiveData;
    MutableLiveData<List<Media>> favoritesLiveData;
    MutableLiveData<Media> singleMediaLiveData;
    MutableLiveData<Throwable> excLiveData;

    public SharedViewModel() {
        mediaApiManager = new MediaApiManager();

        mediaListLiveData = new MutableLiveData<>();
        favoritesLiveData = new MutableLiveData<>();
        singleMediaLiveData = new MutableLiveData<>();
        excLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Media>> getMediaListLiveData() {
        return mediaListLiveData;
    }


    // check if the mediaList from the api contains a favorite media and updating its media type
    public void updateMediaList(List<Media> mediaList, boolean isMovie){
        List<Media> favList = favoritesLiveData.getValue();
        if (favList == null) favList = new ArrayList<>();

        String mediaType = isMovie ? "Movie" : "TV Show";


        for (Media media : mediaList) {
            media.setMediaType(mediaType);
            if (Utils.isInList(media, favList)){
                media.setFavorite(true);
            }
        }
        mediaListLiveData.postValue(mediaList);
    }

    public void setSingleMediaLiveData(Media media){
        if (favoritesLiveData.getValue() != null && Utils.isInList(media, favoritesLiveData.getValue()))
            media.setFavorite(true);
        else
            media.setFavorite(false);

        singleMediaLiveData.postValue(media);
    }

    public void getMediaBySearch(String search, int choice){
       boolean isMovie = (choice == R.id.rbMovie);
       mediaApiManager.getMediaFromApi(this, excLiveData, search, isMovie);
    }


    public LiveData<Media> getSingleMediaLiveData(){
        return singleMediaLiveData;
    }



    public LiveData<List<Media>> getFavoritesLiveData(){
        return favoritesLiveData;
    }


    public void onFavoriteClicked(Media clickedItem) {
        List<Media> mediaList = mediaListLiveData.getValue();
        List<Media> favoriteList = favoritesLiveData.getValue();

        if (favoriteList == null) favoriteList = new ArrayList<>();
        if (mediaList == null) mediaList = new ArrayList<>();

        // update favorite list
        if (!Utils.isInList(clickedItem, favoriteList)) {
            favoriteList.add(clickedItem);
            clickedItem.setFavorite(true);
        } else {
            favoriteList = Utils.removeItemFromList(clickedItem, favoriteList);
            clickedItem.setFavorite(false);
        }

        // Update Media List
        for (Media media : mediaList) {
            media.setFavorite(Utils.isInList(media, favoriteList));
        }

        mediaListLiveData.postValue(mediaList);
        favoritesLiveData.postValue(favoriteList);
    }





    public void pushDataToFirebase(List<Media> list, String path){
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Gson convert = new Gson();
        String data = convert.toJson(list);
        FirebaseDatabase.getInstance().getReference(path)
                .child(userID).setValue(data);
    }


    public void postFavorite(List<Media> list){
        favoritesLiveData.postValue(list);
    }

    public void postMediaList(List<Media> list){
        mediaListLiveData.postValue(list);
    }


}