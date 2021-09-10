package dev.haguel.mymediaapp.ui.main.base;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import dev.haguel.mymediaapp.ui.main.activities.MainActivity;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.models.Media;
import dev.haguel.mymediaapp.ui.main.viewmodel.SharedViewModel;

public abstract class BaseViewPagerPage extends Fragment {


    protected SharedViewModel getDataModel() {
        SharedViewModel retVal = ((MainActivity)getActivity()).mViewModel;
        return retVal;
    }

    protected EventListener eventListener;

    protected void toggleLoader(boolean isVisible) {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity)getActivity();
            activity.toggleLoader(isVisible);
        }
    }

}
