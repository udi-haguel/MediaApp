package dev.haguel.mymediaapp.ui.main.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.adapters.MediaAdapter;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class FavoritesFragment extends BaseFragment {

    public static final String FAVORITE_LIST_KEY = "FAVORITE_LIST_KEY";

    public static FavoritesFragment newInstance(EventListener eventListener, ArrayList<Media> favList) {

        FavoritesFragment favFrag = new FavoritesFragment();
        favFrag.eventListener = eventListener;

        Bundle bundle = new Bundle();
        bundle.putSerializable(FAVORITE_LIST_KEY, favList);
        favFrag.setArguments(bundle);

        return favFrag;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) return;
        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(FAVORITE_LIST_KEY)) return;

        ArrayList<Media> favoriteList = (ArrayList<Media>) bundle.getSerializable(FAVORITE_LIST_KEY);
        RecyclerView rvMedia = view.findViewById(R.id.rvFavoriteMedia);
        MediaAdapter adapter = new MediaAdapter(favoriteList);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedia.setAdapter(adapter);
        adapter.setOnMediaClickListener(new MediaAdapter.OnMediaClickListener() {
            @Override
            public void onMediaClick(int position) {
                eventListener.onMediaClickedListener(favoriteList.get(position));
            }

            @Override
            public void onFavoriteClick(int position) {
                eventListener.onFavoriteClickListener(favoriteList.get(position));
            }
        });


    }

}