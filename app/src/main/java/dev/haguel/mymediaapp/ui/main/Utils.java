package dev.haguel.mymediaapp.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.screens.RegisterFragment;

public class Utils {
    public static boolean isInList(Media media, List<Media> list){
        for (Media item : list) {
            if (compareID(media, item)){
                return true;
            }
        }
        return false;
    }

    public static boolean compareID(Media media1, Media media2){
        return media1.getId() == media2.getId();
    }

    public static List<Media> removeItemFromList(Media item, List<Media> list){
        int itemIndex = getIndexOfMediaFromList(item, list);
        list.remove(itemIndex);
        return list;
    }

    public static int getIndexOfMediaFromList(Media item, List<Media> list){
        int itemIndex = 0;
        for (Media media : list) {
            if (compareID(media, item)) break;
            itemIndex++;
        }
        return itemIndex;
    }

    public static void changeAuthScreen(FragmentTransaction ft, Fragment fragment){
        ft.replace(R.id.auth_fragment_container, fragment);
        ft.commit();
    }
}
