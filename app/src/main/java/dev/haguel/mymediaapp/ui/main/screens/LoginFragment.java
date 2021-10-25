package dev.haguel.mymediaapp.ui.main.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.activities.MainActivity;

public class LoginFragment extends BaseAuthFragment {

    public static final String EMAIL_KEY = "EMAIL_KEY";

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;

    private FirebaseAuth mAuth;


    public static LoginFragment newInstance(String email) {
        LoginFragment loginFrag = new LoginFragment();

        Bundle bundle = new Bundle();
        bundle.putString(EMAIL_KEY, email);
        loginFrag.setArguments(bundle);

        return loginFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email;
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EMAIL_KEY)) {
            email = bundle.getString(EMAIL_KEY);
        } else {
            email = "";
        }

        etLoginEmail = view.findViewById(R.id.etLoginEmail);
        etLoginPassword = view.findViewById(R.id.etLoginPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegisterBtn);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);


        etLoginEmail.setText(email);

        btnLogin.setOnClickListener(v -> {
            inputValidateAndLoginUser();
        });

        tvRegister.setOnClickListener(v -> {
            Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), RegisterFragment.newInstance());
        });

        tvForgotPassword.setOnClickListener(v -> {
            Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), ForgotPasswordFragment.newInstance());
        });



        mAuth = FirebaseAuth.getInstance();


    }


    private void inputValidateAndLoginUser() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();


        if (email.isEmpty()){
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etLoginEmail.setError("Please enter a valid email");
            etLoginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etLoginPassword.setError("Password is required");
            etLoginPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etLoginPassword.setError("Min password length is 6 characters");
            etLoginPassword.requestFocus();
            return;
        }

        toggleLoader(true);
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        if (user.isEmailVerified()){
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(getActivity(), "check your email to verify your account", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed to log in", Toast.LENGTH_LONG).show();
                    }
                    toggleLoader(false);
                });
    }

}