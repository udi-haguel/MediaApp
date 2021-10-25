package dev.haguel.mymediaapp.ui.main.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class SingleMediaFragment extends BaseFragment {

    private static final String SINGLE_MEDIA_KEY = "SINGLE_MEDIA";
    private ImageView ivBackdropPath;
    private ImageView ivSingleFavorite;
    private TextView tvSingleTitle;
    private TextView tvSingleMediaType;
    private TextView tvSingleReleaseDate;
    private TextView tvSingleRatingsAndVoteAverage;
    private TextView tvSingleMediaOverview;

    //private AccountViewModel mViewModel;
    public static SingleMediaFragment newInstance(EventListener eventListener, Media singleMedia) {

        Bundle args = new Bundle();

        if (singleMedia !=null)
            args.putSerializable(SINGLE_MEDIA_KEY, singleMedia);

        SingleMediaFragment mediaFrag = new SingleMediaFragment();
        mediaFrag.setArguments(args);
        mediaFrag.eventListener = eventListener;
        return mediaFrag;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_media_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) return;
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(SINGLE_MEDIA_KEY)) return;

        Media media = (Media) bundle.getSerializable(SINGLE_MEDIA_KEY);
        if (media == null) {
            return;
        }

        ivBackdropPath = view.findViewById(R.id.ivBackdropPath);
        ivSingleFavorite = view.findViewById(R.id.ivSingleMediaFav);
        tvSingleTitle = view.findViewById(R.id.tvSingleTitle);
        tvSingleMediaType = view.findViewById(R.id.tvSingleMediaType);
        tvSingleReleaseDate = view.findViewById(R.id.tvSingleReleaseDate);
        tvSingleRatingsAndVoteAverage = view.findViewById(R.id.tvSingleRatingsAndVoteAverage);
        tvSingleMediaOverview = view.findViewById(R.id.tvSingleMediaOverview);



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


}