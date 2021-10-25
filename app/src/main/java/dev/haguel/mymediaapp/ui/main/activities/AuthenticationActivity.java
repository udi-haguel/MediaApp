package dev.haguel.mymediaapp.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.screens.LoginFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private FrameLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        loader = findViewById(R.id.flLoaderAuth);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.auth_fragment_container, LoginFragment.newInstance(""));
        fragmentTransaction.commit();
    }



    public void toggleLoader(boolean show){

        if (show) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }


}