package dev.haguel.mymediaapp.ui.main.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MediaService {

    @GET("search/movie")
    Call<MediaResponse> geMovies(@Query("api_key") String apiKey, @Query("query") String searchText);


    @GET("search/tv")
    Call<MediaResponse> getTVShows(@Query("api_key") String apiKey, @Query("query") String searchText);

    @GET("discover")
    Call<MediaResponse> getDiscover(@Query("api_key") String apiKey);
}




/// https://api.themoviedb.org/3/search/movie?api_key=96624ea86553cd7a4caed4ecbdc35ec1&query=deadpool