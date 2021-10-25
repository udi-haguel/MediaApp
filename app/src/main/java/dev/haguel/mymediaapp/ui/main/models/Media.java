package dev.haguel.mymediaapp.ui.main.models;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Media implements Serializable {

    // ID OF THE MEDIA
    @SerializedName("id")
    private int id;
// {"original_title", "original_name"})
    // MEDIA NAME
    @SerializedName(value="title", alternate = "name")
    private String title;

    // MEDIA POSTER - for recycler
    @SerializedName("poster_path")
    private String posterPath;

    // MEDIA BACKGROUND - for single media view
    @SerializedName("backdrop_path")
    private String backdropPath;

    // MEDIA OVERVIEW
    @SerializedName("overview")
    private String overview;

    // MEDIA VOTERS COUNT
    @SerializedName("vote_count")
    private int voteCount;

    // MEDIA RELEASE DATE
    @SerializedName(value="releaseDate", alternate = {"release_date", "first_air_date"})
    private String releaseDate;

    // MEDIA RATINGS
    @SerializedName("vote_average")
    private double voteAverage;

    private boolean isFavorite = false;
    private String mediaType = "";

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }



    public Media() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean getFavorite(){
        return isFavorite;
    }
    public void changeFavoriteStatus(){
        isFavorite = !isFavorite;
    }
    public void setFavorite(boolean isFavorite){

        this.isFavorite = isFavorite;
    }
    public void removeFavorite(){
        isFavorite = false;
    }

    //returns "Ratings: x"
    public String getRatings(){
        if (voteAverage == 0) return "No ratings";
        String[] ratings = String.valueOf(voteAverage).split("[.]");
        if (ratings[1].equals("0"))
            return "Ratings: " + ratings[0];
        else
            return "Ratings: " + String.valueOf(voteAverage);
    }

    //get year from the release date
    public String getYear(){
        if (releaseDate == null) return "";
        String[] year = releaseDate.split("-");
        return year[0];
    }

    //change date from yyyy-mm-dd to dd/mm/yyyy using split with regex
    public String getDate(){
        if (releaseDate == null || releaseDate.equals("")) return "No release date";
        String[] date = releaseDate.split("-");
        return date[2] + "/" + date[1] + "/" + date[0];
    }


    public String getRatingsWithVotersCount(){
        if (voteAverage == 0) return "No ratings";
        return getRatings() + " / " + voteCount + " voters";
    }

    // getting the image path link
    public String getImageLinkPath(){
        return "https://image.tmdb.org/t/p/w500";
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", overview='" + overview + '\'' +
                ", voteCount=" + voteCount +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                '}';
    }

}
