package dev.haguel.mymediaapp.ui.main.models;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dev.haguel.mymediaapp.ui.main.viewmodel.SharedViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaApiManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final MediaService service = retrofit.create(MediaService.class);
    private static final String API_KEY = "96624ea86553cd7a4caed4ecbdc35ec1";


    public void getMediaFromApi(SharedViewModel model, MutableLiveData<Throwable> excLiveData, String query, boolean isMovie){
        Call<MediaResponse> httpRequest;
        if (isMovie)
            httpRequest = service.geMovies(API_KEY, query);
        else
            httpRequest = service.getTVShows(API_KEY, query);

        httpRequest.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(@NotNull Call<MediaResponse> call, @NotNull Response<MediaResponse> response) {
                MediaResponse mediaResponse = response.body();

                if (mediaResponse != null){
                    ArrayList<Media> mediaList = mediaResponse.getMediaList();
                    model.updateMediaList(mediaList, isMovie);
                }
            }
            @Override
            public void onFailure(@NotNull Call<MediaResponse> call, @NotNull Throwable t) {
                excLiveData.postValue(t);
            }
        });
    }


    public void getDiscoverMoviesFromApi(SharedViewModel model, MutableLiveData<Throwable> excLiveData){
        Call<MediaResponse> httpRequest;
        httpRequest = service.getDiscover(API_KEY);

        httpRequest.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                MediaResponse mediaResponse = response.body();

                if (mediaResponse != null){
                    ArrayList<Media> mediaList = mediaResponse.getMediaList();
                    model.updateMediaList(mediaList, true);
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                excLiveData.postValue(t);
            }
        });
    }


}
