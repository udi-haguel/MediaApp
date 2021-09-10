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
import dev.haguel.mymediaapp.ui.main.base.BaseViewPagerPage;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;

public class FavoritesViewPagerPage extends BaseViewPagerPage {


    RecyclerView rvMedia;
    ArrayList<Media> favList;
    public static FavoritesViewPagerPage newInstance(EventListener eventListener, ArrayList<Media> favList) {

        FavoritesViewPagerPage favFrag = new FavoritesViewPagerPage();
        favFrag.eventListener = eventListener;
        favFrag.favList = favList;
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
        rvMedia = view.findViewById(R.id.rvFavoriteMedia);
        MediaAdapter adapter = new MediaAdapter(favList);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedia.setAdapter(adapter);
        adapter.setOnMediaClickListener(new MediaAdapter.OnMediaClickListener() {
            @Override
            public void onMediaClick(int position) {
                eventListener.onMediaClickedListener(favList.get(position));
                MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFavoriteClick(int position) {
                eventListener.onFavoriteClickListener(favList.get(position));
                MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void notifyListChanged(ArrayList<Media> list) {
        if (rvMedia == null) return;
        this.favList.clear();
        this.favList.addAll(list);
        MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
        adapter.notifyDataSetChanged();
    }

}