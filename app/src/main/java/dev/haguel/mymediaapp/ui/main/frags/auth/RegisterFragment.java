package dev.haguel.mymediaapp.ui.main.frags.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.Utils;
import dev.haguel.mymediaapp.ui.main.activities.AuthenticationActivity;
import dev.haguel.mymediaapp.ui.main.models.User;

public class RegisterFragment extends Fragment {

    EditText etRegisterName;
    EditText etRegisterEmail;
    EditText etRegisterPassword;
    EditText etRegisterPassword2;
    Button btnRegister;
    TextView tvAlreadyMember;

    private FirebaseAuth mAuth;


    public static RegisterFragment newInstance() {
        RegisterFragment registerFrag = new RegisterFragment();
        return registerFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etRegisterName = view.findViewById(R.id.etRegisterName);
        etRegisterEmail = view.findViewById(R.id.etRegisterEmail);
        etRegisterPassword = view.findViewById(R.id.etRegisterPassword);
        etRegisterPassword2 = view.findViewById(R.id.etRegisterPassword2);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvAlreadyMember = view.findViewById(R.id.tvAlreadyMember);

        mAuth = FirebaseAuth.getInstance();


        tvAlreadyMember.setOnClickListener(v -> {
            Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), LoginFragment.newInstance(""));
        });


        btnRegister.setOnClickListener(v->{
            checkUserValidationAndRegister();
        });
    }

    private void checkUserValidationAndRegister(){
        String fullName = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String password2 = etRegisterPassword2.getText().toString().trim();

        if (fullName.isEmpty()){
            etRegisterName.setError("Full name is required!");
            etRegisterName.requestFocus();
            return;
        }
        if (email.isEmpty()){
            etRegisterEmail.setError("Email is required!");
            etRegisterEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etRegisterEmail.setError("Please provide valid email");
            etRegisterEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etRegisterPassword.setError("Passowrd is required");
            etRegisterPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            etRegisterPassword.setError("Min password length should be 6 characters");
            etRegisterPassword.requestFocus();
            return;
        }
        if (password2.isEmpty()){
            etRegisterPassword2.setError("Required field");
            etRegisterPassword2.requestFocus();
            return;
        }
        if (!password.equals(password2)){
            etRegisterPassword2.setError("Password does not match");
            etRegisterPassword2.requestFocus();
            return;
        }

        ((AuthenticationActivity)getActivity()).toggleDialog(true);
        registerUser(fullName, email, password);
    }

    public void registerUser(String fullName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registerTask -> {

                    if (registerTask.isSuccessful()){
                        User user = new User(fullName, email);
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();;
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                        FirebaseDatabase.getInstance().getReference(Utils.USERS_FIREBASE_KEY)
                                .child(userID).setValue(user);

                        FirebaseDatabase.getInstance().getReference(Utils.FAVORITES_FIREBASE_KEY)
                                .child(userID).setValue("[]");

                        FirebaseDatabase.getInstance().getReference(Utils.LAST_SEARCH_FIREBASE_KEY)
                                .child(userID).setValue("[]");

                        FirebaseAuth.getInstance().signOut();
                        ((AuthenticationActivity)getActivity()).toggleDialog(false);
                        Toast.makeText(getActivity(), "Please verify your Email and log in", Toast.LENGTH_LONG).show();
                        Utils.changeAuthScreen(getParentFragmentManager().beginTransaction(), LoginFragment.newInstance(etRegisterEmail.getText().toString().trim()));
                    } else {
                        Toast.makeText(getActivity(), "Failed to register", Toast.LENGTH_LONG).show();
                        ((AuthenticationActivity)getActivity()).toggleDialog(false);
                    }
                });
    }
}