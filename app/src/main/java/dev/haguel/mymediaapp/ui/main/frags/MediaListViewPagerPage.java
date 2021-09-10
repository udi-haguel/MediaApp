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

public class MediaListViewPagerPage extends BaseViewPagerPage {


    public static MediaListViewPagerPage newInstance(EventListener eventListener, ArrayList<Media> mediaList) {
        MediaListViewPagerPage mediaListFrag = new MediaListViewPagerPage();
        mediaListFrag.eventListener = eventListener;
        mediaListFrag.mediaList = mediaList;
        return mediaListFrag;
    }


    // Data
    ArrayList<Media> mediaList = new ArrayList<>();

    // UI
    RecyclerView rvMedia;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.media_list_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMedia = view.findViewById(R.id.rvMedia);
        MediaAdapter adapter = new MediaAdapter(mediaList);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedia.setAdapter(adapter);
        adapter.setOnMediaClickListener(new MediaAdapter.OnMediaClickListener() {
            @Override
            public void onMediaClick(int position) {
                eventListener.onMediaClickedListener(mediaList.get(position));
                MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFavoriteClick(int position) {
                eventListener.onFavoriteClickListener(mediaList.get(position));
                MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });
        rvMedia.scrollToPosition(0);
        toggleLoader(false);
    }

    public void notifyListChanged(ArrayList<Media> list) {
        if (rvMedia == null) return;
        this.mediaList.clear();
        this.mediaList.addAll(list);
        MediaAdapter adapter = (MediaAdapter) rvMedia.getAdapter();
        adapter.notifyDataSetChanged();
    }

}