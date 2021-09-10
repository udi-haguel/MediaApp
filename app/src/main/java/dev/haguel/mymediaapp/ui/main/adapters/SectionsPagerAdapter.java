package dev.haguel.mymediaapp.ui.main.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.activities.MainActivity;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.frags.SingleMediaViewPagerPage;
import dev.haguel.mymediaapp.ui.main.frags.FavoritesViewPagerPage;
import dev.haguel.mymediaapp.ui.main.frags.MediaListViewPagerPage;
import dev.haguel.mymediaapp.ui.main.frags.SearchViewPagerPage;


public class SectionsPagerAdapter extends FragmentStateAdapter {

    private MainActivity activityRef;
    private HashMap<Integer, Fragment> fragsMap = new HashMap<>();

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.search, R.string.list, R.string.favorites, R.string.single_media};

    EventListener eventListener; // Callback Listener For Events In Fragments

    /*
    List<Media> mediaList = new ArrayList<>();
    List<Media> favoriteList = new ArrayList<>();
    Media singleMedia;
    */

    public SectionsPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activityRef = (MainActivity) fragmentActivity;
        this.eventListener = (MainActivity) fragmentActivity;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {

        Fragment frag = null;
        switch (position){
            case 0:
                frag = SearchViewPagerPage.newInstance(eventListener);
                break;
            case 1:
                frag = MediaListViewPagerPage.newInstance(eventListener, new ArrayList<Media>(activityRef.mViewModel.getMediaListLiveData().getValue()));
                break;
            case 2:
                frag = FavoritesViewPagerPage.newInstance(eventListener, new ArrayList<Media>(activityRef.mViewModel.getFavoritesLiveData().getValue()));
                break;
            case 3:
                frag = SingleMediaViewPagerPage.newInstance(eventListener, activityRef.mViewModel.getSingleMediaLiveData().getValue());
                break;
            default:
                throw new IllegalArgumentException("No such fragment");
        }
        if (frag != null){
            fragsMap.put(position, frag);
        }
        return frag;
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public Fragment getFragmentByIndex(int pos) {
        return fragsMap.get(pos);
    }
}

