package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AudioCallFragment extends Fragment {

    // Variables
    private Button audioCallStatusACBTN;
    private TextView informationACTV;
    private RecyclerView audioCallHistoryACRV;
    private RotateLoading mLoadingRL;
    // Variables

    // Methods
    private void init(View view) {
        audioCallStatusACBTN = view.findViewById(R.id.audioCallStatusACBTN);
        informationACTV = view.findViewById(R.id.informationACTV);
        audioCallHistoryACRV = view.findViewById(R.id.audioCallHistoryACRV);
        mLoadingRL = view.findViewById(R.id.mLoadingRL);
    }

    public void setAudioCallStatus() {
        AudioCallController.CheckAudioCallStatus(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                String status = (String) object;
                audioCallStatusACBTN.setText(status);
            }
        });
    }

    private void event() {
        audioCallStatusACBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioCallStatusACBTN.getText().equals(AFModel.online)) {
                    AudioCallController.SetAudioCallDoctor(AFModel.offline);
                } else {
                    AudioCallController.SetAudioCallDoctor(AFModel.online);
                }
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_audio_call, container, false);
        init(view);
        event();
        setAudioCallStatus();
        return view;
    }
}
