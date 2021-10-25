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

public class MediaListFragment extends BaseFragment {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST_KEY";


    public static MediaListFragment newInstance(EventListener eventListener, ArrayList<Media> mediaList) {
        MediaListFragment mediaListFrag = new MediaListFragment();
        mediaListFrag.eventListener = eventListener;

        Bundle bundle = new Bundle();
        bundle.putSerializable(MEDIA_LIST_KEY, mediaList);
        mediaListFrag.setArguments(bundle);

        return mediaListFrag;

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.media_list_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) return;

        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(MEDIA_LIST_KEY)) {
            return;
        }

        ArrayList<Media> mediaList = (ArrayList<Media>) bundle.getSerializable(MEDIA_LIST_KEY);
        RecyclerView rvMedia = view.findViewById(R.id.rvMedia);
        MediaAdapter adapter = new MediaAdapter(mediaList);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedia.setAdapter(adapter);
        adapter.setOnMediaClickListener(new MediaAdapter.OnMediaClickListener() {
            @Override
            public void onMediaClick(int position) {
                eventListener.onMediaClickedListener(mediaList.get(position));
            }

            @Override
            public void onFavoriteClick(int position) {
                eventListener.onFavoriteClickListener(mediaList.get(position));
            }
        });
        rvMedia.scrollToPosition(0);
        toggleLoader(false);

    }
}