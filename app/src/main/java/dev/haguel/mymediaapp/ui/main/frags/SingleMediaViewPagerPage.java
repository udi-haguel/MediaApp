package dev.haguel.mymediaapp.ui.main.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.base.BaseViewPagerPage;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class SingleMediaViewPagerPage extends BaseViewPagerPage {

    // DATA
    Media media;

    // UI
    private ImageView ivBackdropPath;
    private ImageView ivSingleFavorite;
    private TextView tvSingleTitle;
    private TextView tvSingleMediaType;
    private TextView tvSingleReleaseDate;
    private TextView tvSingleRatingsAndVoteAverage;
    private TextView tvSingleMediaOverview;

    public static SingleMediaViewPagerPage newInstance(EventListener eventListener, Media singleMedia) {
        SingleMediaViewPagerPage mediaFrag = new SingleMediaViewPagerPage();
        mediaFrag.eventListener = eventListener;
        mediaFrag.media = singleMedia;
        return mediaFrag;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_media_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivBackdropPath = view.findViewById(R.id.ivBackdropPath);
        ivSingleFavorite = view.findViewById(R.id.ivSingleMediaFav);
        tvSingleTitle = view.findViewById(R.id.tvSingleTitle);
        tvSingleMediaType = view.findViewById(R.id.tvSingleMediaType);
        tvSingleReleaseDate = view.findViewById(R.id.tvSingleReleaseDate);
        tvSingleRatingsAndVoteAverage = view.findViewById(R.id.tvSingleRatingsAndVoteAverage);
        tvSingleMediaOverview = view.findViewById(R.id.tvSingleMediaOverview);


        if (media == null) return;
        initDataToUI();
    }

    private void initDataToUI() {
        tvSingleTitle.setText(media.getTitle());
        tvSingleMediaType.setText(media.getMediaType());
        tvSingleReleaseDate.setText(media.getDate());
        tvSingleRatingsAndVoteAverage.setText(media.getRatingsWithVotersCount());
        tvSingleMediaOverview.setText(media.getOverview());
        ivSingleFavorite.setOnClickListener(ivFavIconView -> {
            eventListener.onFavoriteClickListener(media);
        });
        Picasso.get().load(media.getImageLinkPath() + media.getBackdropPath())
                .placeholder(R.mipmap.preview_backdrop_image)
                .error(R.mipmap.preview_backdrop_image)
                .into(ivBackdropPath, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        toggleLoader(false);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        favoriteToggle(media);
    }


    private void favoriteToggle(Media media){
        if (media.getFavorite()){
            ivSingleFavorite.setImageResource(R.drawable.star_yellow);
        } else {
            ivSingleFavorite.setImageResource(R.drawable.star_outline);
        }
    }

    public void notifyListChanged(Media media) {
        this.media = media;
        initDataToUI();
    }


}