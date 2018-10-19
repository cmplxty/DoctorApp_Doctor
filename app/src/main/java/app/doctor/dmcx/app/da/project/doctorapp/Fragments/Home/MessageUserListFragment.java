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
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.MessageUserRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.MessageController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.MessageUser;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class MessageUserListFragment extends Fragment {

    // Variables
    private RecyclerView messagesHFMRV;
    private RotateLoading mLoadingRL;

    private MessageUserRecyclerViewAdapter messageUserRecyclerViewAdapter;
    // Variables

    // Methods
    private void init(View view) {
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        messagesHFMRV = view.findViewById(R.id.messagesHFMRV);
        messagesHFMRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        messagesHFMRV.setHasFixedSize(true);

        messageUserRecyclerViewAdapter = new MessageUserRecyclerViewAdapter();
        messagesHFMRV.setAdapter(messageUserRecyclerViewAdapter);
    }

    private void load_data() {
        mLoadingRL.start();

        MessageController.LoadMessgeUsersList(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object instanceof String) {
                    String errorCode = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MessageUser> messageUsers = new ArrayList<>();
                for (Object data : (List<?>) object) {
                    if (data instanceof MessageUser)
                        messageUsers.add((MessageUser) data);
                }

                messageUserRecyclerViewAdapter.setMessageUsers(messageUsers);
                messageUserRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_message, container, false);

        init(view);

        load_data();

        return view;
    }
}
