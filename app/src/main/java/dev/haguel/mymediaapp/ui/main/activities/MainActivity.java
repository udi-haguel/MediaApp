package dev.haguel.mymediaapp.ui.main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

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
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.LoaderDialog;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.adapters.SectionsPagerAdapter;
import dev.haguel.mymediaapp.ui.main.frags.FavoritesViewPagerPage;
import dev.haguel.mymediaapp.ui.main.frags.MediaListViewPagerPage;
import dev.haguel.mymediaapp.ui.main.frags.SingleMediaViewPagerPage;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.viewmodel.SharedViewModel;


public class MainActivity extends FragmentActivity implements EventListener {




    public static final int[] ICONS = new int[]{R.drawable.search, R.drawable.list, R.drawable.star, R.drawable.filmstrip};

    // DATA
    public SharedViewModel mViewModel;

    // UI
    private int currentPageIndex;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabs;
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
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabsLayout);

        // Init FireBase
        this.initFirebase();

        loaderDialog = new LoaderDialog(this);
        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sectionsPagerAdapter = new SectionsPagerAdapter(this);

        viewPager.setAdapter(sectionsPagerAdapter);
        // Init Tab Bar Layout
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
        }).attach();
        setIcons(tabs);




        mViewModel.getMediaListLiveData().observe(this, mediaList -> {
            if (mediaList != null && mediaList.size() > 0) {
                mViewModel.pushDataToFirebase(mediaList, Utils.LAST_SEARCH_FIREBASE_KEY);
                // sectionsPagerAdapter.setMediaList(mediaList);
                if (currentPageIndex == 0 && isOnSearch) {
                    isOnSearch = false;
                    moveToTab(1);
                }
                //sectionsPagerAdapter.notifyDataSetChanged();
            }
             toggleLoader(false);
            setIcons(tabs);
        });

        mViewModel.getFavoritesLiveData().observe(this, favList -> {
            if (favList != null) {
                mViewModel.pushDataToFirebase(favList, Utils.FAVORITES_FIREBASE_KEY);
                // sectionsPagerAdapter.setFavoriteList(favList);
                //sectionsPagerAdapter.notifyDataSetChanged();
            }
            setIcons(tabs);
        });


        mViewModel.getSingleMediaLiveData().observe(this, media -> {
            if (!isSingleMediaExist) {
                TabLayout.Tab tabAt = tabs.getTabAt(3);
                if (tabAt != null) {
                    isSingleMediaExist = true;
                    tabAt.view.setClickable(true);
                }
            }
            updateFragmentsData();
            // sectionsPagerAdapter.setSelectedMedia(media);
            //sectionsPagerAdapter.notifyDataSetChanged();
            setIcons(tabs);
        });

        viewPager.registerOnPageChangeCallback(new OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPageIndex = position;
                updateFragmentsData();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        TabLayout.Tab tabAt = tabs.getTabAt(3);
        if (tabAt != null) {
            tabAt.view.setClickable(false);
        }
        setPageTitle(R.string.search);


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
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

        for (int i = 0; i < menu.size(); i++) {
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


    private void initFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, AuthenticationActivity.class));
        } else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            dbRef = FirebaseDatabase.getInstance().getReference();
            userID = user.getUid();
        }
    }

    private void updateFragmentsData() {
        if (currentPageIndex == 1) {
            // Update Media List Fragment
            MediaListViewPagerPage frag = (MediaListViewPagerPage) sectionsPagerAdapter.getFragmentByIndex(1);
            if (frag != null) {
                ArrayList<Media> list = new ArrayList<>(mViewModel.getMediaListLiveData().getValue());
                if (frag != null)
                    frag.notifyListChanged(list);
            }
        }
        if (currentPageIndex == 2) {
            // Update Fav List Fragment
            FavoritesViewPagerPage frag = (FavoritesViewPagerPage) sectionsPagerAdapter.getFragmentByIndex(2);
            if (frag != null) {
                ArrayList<Media> list = new ArrayList<>(mViewModel.getFavoritesLiveData().getValue());
                if (frag != null)
                    frag.notifyListChanged(list);
            }
        }
        if (currentPageIndex == 3) {
            // Update Fav List Fragment
            SingleMediaViewPagerPage frag = (SingleMediaViewPagerPage) sectionsPagerAdapter.getFragmentByIndex(3);
            if (frag != null) {
                if (frag != null)
                    frag.notifyListChanged(mViewModel.getSingleMediaLiveData().getValue());
            }
        }
    }


    private void moveToTab(int tabIndex) {
        this.currentPageIndex = tabIndex;
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
        if (toShow) {
            if (!loaderDialog.isShowing())
                loaderDialog.show();
        } else {
            if (loaderDialog.isShowing())
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
    }

    @Override
    public void onMediaClickedListener(Media clickedItem) {
        toggleLoader(true);
        mViewModel.setSingleMediaLiveData(clickedItem);
        moveToTab(3);
        // updateFragmentsData();
    }


    public List<Media> snapshotToList(DataSnapshot snapshot) {
        Gson convert = new Gson();
        Type listType = new TypeToken<List<Media>>() {
        }.getType();
        String data = snapshot.getValue(String.class);
        return convert.fromJson(data, listType);
    }

    public void setPageTitle(int stringRes) {
        ((Toolbar)findViewById(R.id.mToolBar)).setTitle(stringRes);
    }
}

