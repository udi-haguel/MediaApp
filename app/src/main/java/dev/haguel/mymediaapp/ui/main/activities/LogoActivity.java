package dev.haguel.mymediaapp.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import dev.haguel.mymediaapp.R;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        Handler handler = new Handler(Looper.getMainLooper());


        handler.postDelayed(()->{
            Intent goToLogin = new Intent(this, MainActivity.class);
            startActivity(goToLogin);
            finish();
        },2000);
    }
}