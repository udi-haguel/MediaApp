package dev.haguel.mymediaapp.ui.main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.LoaderDialog;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.adapters.SectionsPagerAdapter;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.viewmodel.SharedViewModel;



public class MainActivity extends FragmentActivity implements EventListener {



    public static final int[] ICONS = new int[]{R.drawable.search, R.drawable.list, R.drawable.star, R.drawable.filmstrip};


    // DATA
    private SharedViewModel mViewModel;

    // UI
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabs;
    private View loader;
    private Toolbar mToolBar;
    private LoaderDialog loaderDialog;

    // FIREBASE
    private FirebaseUser user;
    private DatabaseReference dbRef;
    private String userID;

    // FLAGS
    private boolean isOnSearch = false;
    private boolean isSingleMediaExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, AuthenticationActivity.class));
        } else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            dbRef = FirebaseDatabase.getInstance().getReference();
            userID = user.getUid();
        }


        mToolBar = findViewById(R.id.mToolBar);
        //setSupportActionBar(mToolBar);

        loaderDialog = new LoaderDialog(this);

        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sectionsPagerAdapter = new SectionsPagerAdapter(this);
        viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(sectionsPagerAdapter);
        // viewPager.setOffscreenPageLimit(4);
        tabs = findViewById(R.id.tabsLayout);
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> {

                }
                ).attach();
        //tabs.setupWithViewPager(viewPager);
        setIcons(tabs);

//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                //mToolBar.setTitle(sectionsPagerAdapter.getTitleId(position));
//                setPageTitle(sectionsPagerAdapter.getTitleId(position));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//
//
//
//
//        mViewModel.getMediaListLiveData().observe(this, mediaList -> {
//
//
//            if (mediaList != null && mediaList.size() > 0) {
//                mViewModel.pushDataToFirebase(mediaList, Utils.LAST_SEARCH_FIREBASE_KEY);
//                sectionsPagerAdapter.setMediaList(mediaList);
//                if (viewPager.getCurrentItem() == 0 && isOnSearch ) {
//                    isOnSearch = false;
//                    moveToTab(1);
//                }
//                sectionsPagerAdapter.notifyDataSetChanged();
//            } else {
//                toggleLoader(false);
//            }
//            setIcons(tabs);
//        });
//
//        mViewModel.getFavoritesLiveData().observe(this, favList -> {
//            if (favList != null) {
//                mViewModel.pushDataToFirebase(favList, Utils.FAVORITES_FIREBASE_KEY);
//                // sectionsPagerAdapter.setFavoriteList(favList);
//                sectionsPagerAdapter.notifyDataSetChanged();
//            }
//            setIcons(tabs);
//        });
//
//
//        mViewModel.getSingleMediaLiveData().observe(this, media -> {
//            if (!isSingleMediaExist){
//                TabLayout.Tab tabAt = tabs.getTabAt(3);
//                if (tabAt != null){
//                    isSingleMediaExist = true;
//                    tabAt.view.setClickable(true);
//                }
//            }
//            // sectionsPagerAdapter.setSelectedMedia(media);
//            sectionsPagerAdapter.notifyDataSetChanged();
//            setIcons(tabs);
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_log_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthenticationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveToTab(int tabIndex) {
        TabLayout.Tab target = tabs.getTabAt(tabIndex);
        if (target != null) {
            target.select();
        }
        setIcons(tabs);
    }

    private void setIcons(TabLayout tabs) {
        for (int i = 0; i < ICONS.length; i++) {
            tabs.getTabAt(i).setIcon(ICONS[i]);
        }
    }

    public void toggleLoader(boolean toShow) {
        if (toShow){
            if (!loaderDialog.isShowing())
                loaderDialog.show();
        } else {
            if(loaderDialog.isShowing())
                loaderDialog.dismiss();
        }
    }


    @Override
    public void onSearchClickListener(String search, int choice) {
        isOnSearch = true;

        mViewModel.getMediaBySearch(search, choice);
    }

    @Override
    public void onFavoriteClickListener(Media clickedItem) {
        mViewModel.onFavoriteClicked(clickedItem);
        sectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMediaClickedListener(Media clickedItem) {
        toggleLoader(true);
        mViewModel.setSingleMediaLiveData(clickedItem);
        sectionsPagerAdapter.notifyDataSetChanged();
        moveToTab(3);
    }




    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    protected void onResume() {
        super.onResume();
        TabLayout.Tab tabAt = tabs.getTabAt(3);
        if (tabAt != null) {
            tabAt.view.setClickable(false);
        }

        if (dbRef == null) return;

        dbRef.child(Utils.FAVORITES_FIREBASE_KEY).child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mViewModel.postFavorite(snapshotToList(snapshot));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        dbRef.child(Utils.LAST_SEARCH_FIREBASE_KEY).child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mViewModel.postMediaList(snapshotToList(snapshot));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });
        setPageTitle(R.string.search);

    }

    public List<Media> snapshotToList(DataSnapshot snapshot){
        Gson convert = new Gson();
        Type listType = new TypeToken<List<Media>>(){}.getType();
        String data = snapshot.getValue(String.class);
        return convert.fromJson(data, listType);
    }

    public void setPageTitle (int stringRes) {
        mToolBar.setTitle(stringRes);
    }
}

