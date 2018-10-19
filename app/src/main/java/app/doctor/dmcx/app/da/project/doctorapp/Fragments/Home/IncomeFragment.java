package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.doctor.dmcx.app.da.project.doctorapp.R;


public class IncomeFragment extends Fragment {

    // Variables
    // Variables

    // Methods
    private void init(View view) {

    }

    private void event() {

    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_income, container, false);

        init(view);
        event();

        return view;
    }
}
