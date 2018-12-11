package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AssistantRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AssistantController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICall;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Assistant;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AssistantFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshAssistantListRSRL;
    private LinearLayout noAssistantViewRLL;
    private RecyclerView assistantRRV;
    private Button createNewAssistantABTN;
    private RotateLoading mLoadingRL;

    private AssistantRecyclerViewAdapter assistantRecyclerViewAdapter;
    private List<Assistant> assistants;
    private AssistantLoaderHandler assistantLoaderHandler;
    private ICall iCall;

    private static class AssistantLoaderHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class AssistantLoaderRunnable implements Runnable {

        @Override
        public void run() {
            if (mLoadingRL.isStart())
                mLoadingRL.stop();

            updateAdadpter(assistants);
            updateLayout(assistants);
        }
    }

    private void init(View view) {
        mLoadingRL = view.findViewById(R.id.mLoadingRL);
        swipeRefreshAssistantListRSRL = view.findViewById(R.id.swipeRefreshAssistantListRSRL);
        noAssistantViewRLL = view.findViewById(R.id.noAssistantViewRLL);
        assistantRRV = view.findViewById(R.id.assistantRRV);
        createNewAssistantABTN = view.findViewById(R.id.createNewAssistantABTN);

        LinearLayoutManager manager = new LinearLayoutManager(RefActivity.refACActivity.get(), LinearLayoutManager.HORIZONTAL, false);
        assistantRRV.setLayoutManager(manager);
        assistantRRV.setHasFixedSize(true);

        assistantRecyclerViewAdapter = new AssistantRecyclerViewAdapter();
        assistantRRV.setAdapter(assistantRecyclerViewAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(assistantRRV);

        assistants = new ArrayList<>();
        assistantLoaderHandler = new AssistantLoaderHandler();
        iCall = assistantRecyclerViewAdapter.getICallPatient();
    }

    private void event() {
        createNewAssistantABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.CreateNewAssistantActivity();
            }
        });

        swipeRefreshAssistantListRSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllAssistants();
            }
        });
    }

    private void loadAllAssistants() {
        mLoadingRL.start();

        AssistantController.LoadAllAssistants(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                assistants = new ArrayList<>();
                if (object != null) {
                    for (Object assistant : (List<?>) object) {
                        if (assistant instanceof Assistant) {
                            Assistant realAssistant = (Assistant) assistant;
                            assistants.add(realAssistant);
                        }
                    }
                }

                assistantLoaderHandler.postDelayed(new AssistantLoaderRunnable(), 2000);

                if (swipeRefreshAssistantListRSRL.isRefreshing())
                    swipeRefreshAssistantListRSRL.setRefreshing(false);
            }
        });
    }

    private void updateLayout(List<Assistant> assistants) {
        if (assistants.size() > 0) {
            assistantRRV.setVisibility(View.VISIBLE);
            noAssistantViewRLL.setVisibility(View.GONE);
        } else {
            assistantRRV.setVisibility(View.GONE);
            noAssistantViewRLL.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdadpter(List<Assistant> assistants) {
        assistantRecyclerViewAdapter.setAssistants(assistants);
        assistantRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_assistant, container, false);

        init(view);
        event();
        loadAllAssistants();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_HS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PermissionNeededForDirectCall, Toast.LENGTH_SHORT).show();
            } else {
                iCall.call();
            }
        }
    }

}
