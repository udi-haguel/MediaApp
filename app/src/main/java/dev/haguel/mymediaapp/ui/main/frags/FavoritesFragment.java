package dev.haguel.mymediaapp.ui.main.frags;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.adapters.MediaAdapter;
import dev.haguel.mymediaapp.ui.main.base.BaseFragment;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class FavoritesFragment extends BaseFragment {



    public static FavoritesFragment newInstance(EventListener eventListener, ArrayList<Media> favList) {

        FavoritesFragment favFrag = new FavoritesFragment();
        favFrag.eventListener = eventListener;

        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.FAVORITE_LIST_KEY, favList);
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
        if (bundle == null || !bundle.containsKey(Utils.FAVORITE_LIST_KEY)) return;

        ArrayList<Media> favoriteList = (ArrayList<Media>) bundle.getSerializable(Utils.FAVORITE_LIST_KEY);
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