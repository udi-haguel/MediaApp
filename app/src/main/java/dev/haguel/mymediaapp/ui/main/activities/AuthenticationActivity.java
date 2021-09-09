package dev.haguel.mymediaapp.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.LoaderDialog;
import dev.haguel.mymediaapp.ui.main.frags.LoginFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private LoaderDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        loaderDialog = new LoaderDialog(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.auth_fragment_container, LoginFragment.newInstance(""));
        fragmentTransaction.commit();
    }



    public void toggleDialog(boolean toShow){
        if (toShow){
            if (!loaderDialog.isShowing())
                loaderDialog.show();
        } else {
            if(loaderDialog.isShowing())
                loaderDialog.dismiss();
        }
    }
}