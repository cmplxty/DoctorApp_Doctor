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

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.BlogRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.BlogController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class BlogFragment extends Fragment {

    private TextView noBlogFoundBFTV;
    private RecyclerView blogsBFRV;
    private BlogRecyclerViewAdapter blogRecyclerViewAdapter;

    private void init(View view) {
        noBlogFoundBFTV = view.findViewById(R.id.noBlogFoundBFTV);
        blogsBFRV = view.findViewById(R.id.blogsBFRV);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RefActivity.refACActivity.get());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        blogsBFRV.setLayoutManager(layoutManager);
        blogsBFRV.setHasFixedSize(true);

        blogRecyclerViewAdapter = new BlogRecyclerViewAdapter();
        blogsBFRV.setAdapter(blogRecyclerViewAdapter);
    }

    private void loadBlog() {
        BlogController.LoadAllBlogs(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                List<Blog> blogs = new ArrayList<>();

                if (object != null) {
                    for (Object blog : (List<?>) object) {
                        if (blog instanceof Blog)
                            blogs.add((Blog) blog);
                    }
                }

                if (blogs.size() > 0) {
                    switchViews(View.VISIBLE, View.GONE);
                } else {
                    switchViews(View.GONE, View.VISIBLE);
                }

                blogRecyclerViewAdapter.setBlogs(blogs);
                blogRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void switchViews(int i1, int i2) {
        blogsBFRV.setVisibility(i1);
        noBlogFoundBFTV.setVisibility(i2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_blog, container, false);
        init(view);
        loadBlog();
        return view;
    }
}
