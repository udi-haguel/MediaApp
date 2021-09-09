package dev.haguel.mymediaapp.ui.main.screens;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import dev.haguel.mymediaapp.R;
import dev.haguel.mymediaapp.ui.main.models.EventListener;
import dev.haguel.mymediaapp.ui.main.viewmodel.SharedViewModel;

public class SearchFragment extends BaseFragment {

    private SharedViewModel mViewModel;
    private Button btnSearch;
    private EditText etSearchTerm;
    private RadioGroup rg;


    public static SearchFragment newInstance(EventListener eventListener) {
        SearchFragment searchFrag = new SearchFragment();
        searchFrag.eventListener = eventListener;
        return searchFrag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) return;
        mViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        btnSearch = view.findViewById(R.id.btnSearch);
        etSearchTerm = view.findViewById(R.id.etSearchTerm);
        rg = view.findViewById(R.id.rgSearchChoice);


        btnSearch.setOnClickListener(v -> {
            String etInput = etSearchTerm.getText().toString();
            int choice = rg.getCheckedRadioButtonId();
            if (TextUtils.isEmpty(etInput)) return;
            if (eventListener != null){
                toggleLoader(true);
                eventListener.onSearchClickListener(etInput,choice);
            }
        });
    }


}