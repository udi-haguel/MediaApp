package dev.haguel.mymediaapp.ui.main.screens;

import androidx.fragment.app.Fragment;
import dev.haguel.mymediaapp.ui.main.activities.AuthenticationActivity;


public class BaseAuthFragment extends Fragment {

    protected void toggleLoader(boolean show) {
        if (getActivity() instanceof AuthenticationActivity) {
            AuthenticationActivity activity = (AuthenticationActivity)getActivity();
            activity.toggleLoader(show);
        }
    }
}
