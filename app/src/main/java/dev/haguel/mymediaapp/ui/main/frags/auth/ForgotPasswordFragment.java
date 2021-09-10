package dev.haguel.mymediaapp.ui.main.frags.auth;


import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.activities.AuthenticationActivity;

public class ForgotPasswordFragment extends Fragment {

    private EditText etEmail;
    private Button btnReset;
    private TextView tvLogin;
    private TextView tvRegister;

    private String emailForLogin = "";

    FirebaseAuth auth;



    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment forgotPasswordFrag = new ForgotPasswordFragment();
        return forgotPasswordFrag;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgot_password_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.etForgotPasswordEmail);
        btnReset = view.findViewById(R.id.btnResetPassword);
        tvLogin = view.findViewById(R.id.tvBackToLogin);
        tvRegister = view.findViewById(R.id.tvBackToRegister);


        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(v->{
            resetPassword();
        });

        tvLogin.setOnClickListener(v->{
            Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), LoginFragment.newInstance(emailForLogin));
        });

        tvRegister.setOnClickListener(v->{
            Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), RegisterFragment.newInstance());
        });




    }

    private void resetPassword(){
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide a valid email");
            etEmail.requestFocus();
            return;
        }

        ((AuthenticationActivity)getActivity()).toggleDialog(true);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Toast.makeText(getContext(), "Check your email to reset your password", Toast.LENGTH_LONG).show();
               emailForLogin = etEmail.getText().toString().trim();
               etEmail.setText("");
           } else {
               Toast.makeText(getContext(), "Try again! something went wrong, or email does not exist", Toast.LENGTH_LONG).show();
           }
            ((AuthenticationActivity)getActivity()).toggleDialog(false);
        });



    }

}
