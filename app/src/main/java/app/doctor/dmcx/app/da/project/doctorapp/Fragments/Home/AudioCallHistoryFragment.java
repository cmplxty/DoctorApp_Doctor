package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AudioCallHistoryRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.AudioCallHistory;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class AudioCallHistoryFragment extends Fragment {

    // Variables
    private TextView informationACTV;
    private RecyclerView audioCallHistoryACRV;
    private RotateLoading mLoadingRL;

    private AudioCallHistoryRecyclerViewAdapter audioCallHistoryRecyclerViewAdapter;
    private List<AudioCallHistory> histories = new ArrayList<>();
    // Variables

    // Methods
    private void init(View view) {
        informationACTV = view.findViewById(R.id.informationACTV);
        audioCallHistoryACRV = view.findViewById(R.id.audioCallHistoryACRV);
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RefActivity.refACActivity.get());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        audioCallHistoryACRV.setLayoutManager(layoutManager);
        audioCallHistoryACRV.setHasFixedSize(true);

        audioCallHistoryRecyclerViewAdapter = new AudioCallHistoryRecyclerViewAdapter();
        audioCallHistoryACRV.setAdapter(audioCallHistoryRecyclerViewAdapter);
    }

    private void loadAllAudioCallHistory() {
        mLoadingRL.start();

        AudioCallController.RetriveAudioCallHistory(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();
                histories = new ArrayList<>();

                if (object != null) {
                    for (Object history : (List<?>) object) {
                        if (history != null) {
                            histories.add((AudioCallHistory) history);
                        }
                    }
                }

                updateAdapter(histories);
                updateLayout(histories);
            }
        });
    }

    private void updateLayout(List<AudioCallHistory> histories) {
        if (histories.size() > 0) {
            audioCallHistoryACRV.setVisibility(View.VISIBLE);
            informationACTV.setVisibility(View.GONE);
        } else {
            informationACTV.setVisibility(View.VISIBLE);
            audioCallHistoryACRV.setVisibility(View.GONE);
        }
    }

    private void updateAdapter(List<AudioCallHistory> histories) {
        audioCallHistoryRecyclerViewAdapter.setHistories(histories);
        audioCallHistoryRecyclerViewAdapter.notifyDataSetChanged();
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_audio_call_history, container, false);
        init(view);
        loadAllAudioCallHistory();
        return view;
    }
}
