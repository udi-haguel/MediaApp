package dev.haguel.mymediaapp.ui.main.adapters;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.frags.SingleMediaFragment;
import dev.haguel.mymediaapp.ui.main.frags.FavoritesFragment;
import dev.haguel.mymediaapp.ui.main.frags.MediaListFragment;
import dev.haguel.mymediaapp.ui.main.frags.SearchFragment;


public class SectionsPagerAdapter extends FragmentStateAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.search, R.string.list, R.string.favorites, R.string.single_media};

    EventListener eventListener;
    List<Media> mediaList = new ArrayList<>();
    List<Media> favoriteList = new ArrayList<>();
    Media singleMedia;

    public SectionsPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return SearchFragment.newInstance(eventListener);
            case 1:
                return MediaListFragment.newInstance(eventListener, new ArrayList<Media>(mediaList));
            case 2:
                return FavoritesFragment.newInstance(eventListener, new ArrayList<Media>(favoriteList));
            case 3:
                return SingleMediaFragment.newInstance(eventListener, singleMedia);
            default:
                throw new IllegalArgumentException("No such fragment");
        }
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }



    /*
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.search, R.string.list, R.string.favorites, R.string.single_media};

    EventListener eventListener;
    List<Media> mediaList = new ArrayList<>();
    List<Media> favoriteList = new ArrayList<>();
    Media singleMedia;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        eventListener = (EventListener) context;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SearchFragment.newInstance(eventListener);
            case 1:
                return MediaListFragment.newInstance(eventListener, new ArrayList<Media>(mediaList));
            case 2:
                return FavoritesFragment.newInstance(eventListener, new ArrayList<Media>(favoriteList));
            case 3:
                return SingleMediaFragment.newInstance(eventListener, singleMedia);
            default:
                throw new IllegalArgumentException("No such fragment");
        }
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList.clear();
        this.mediaList.addAll(mediaList);
    }

    public int getTitleId(int pos){
        return TAB_TITLES[pos];
    }

    public void setFavoriteList(List<Media> mediaList) {
        this.favoriteList.clear();
        this.favoriteList.addAll(mediaList);
    }

    public void setSelectedMedia(Media singleMedia) {
        this.singleMedia = singleMedia;
        this.singleMedia.setFavorite(Utils.isInList(singleMedia, favoriteList));
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }

     */

}

