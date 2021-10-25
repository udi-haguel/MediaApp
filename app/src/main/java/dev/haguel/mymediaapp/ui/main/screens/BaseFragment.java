package dev.haguel.mymediaapp.ui.main.screens;

import androidx.fragment.app.Fragment;

import dev.haguel.mymediaapp.ui.main.activities.MainActivity;
import dev.haguel.mymediaapp.ui.main.models.EventListener;

public abstract class BaseFragment extends Fragment {
    protected EventListener eventListener;

    protected void toggleLoader(boolean show) {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity)getActivity();
            activity.toggleLoader(show);
        }
    }
}
