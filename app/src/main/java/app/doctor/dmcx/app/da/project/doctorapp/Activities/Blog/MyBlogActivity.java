package app.doctor.dmcx.app.da.project.doctorapp.Activities.Blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.MyBlogRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.BlogController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MyBlogActivity extends AppCompatActivity {

    public static WeakReference<MyBlogActivity> instance;

    private Toolbar toolbar;
    private LinearLayout noBlogMBCL;
    private Button writeBlogNowMBABTN;
    private RecyclerView myBlogMBARV;
    private MyBlogRecyclerViewAdapter myBlogRecyclerViewAdapter;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        noBlogMBCL = findViewById(R.id.noBlogMBALL);
        writeBlogNowMBABTN = findViewById(R.id.writeBlogNowMBABTN);
        myBlogMBARV = findViewById(R.id.myBlogMBARV);
        myBlogMBARV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        myBlogMBARV.setHasFixedSize(true);

        myBlogRecyclerViewAdapter = new MyBlogRecyclerViewAdapter();
        myBlogMBARV.setAdapter(myBlogRecyclerViewAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void event() {
        writeBlogNowMBABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BlogEditorActivity(Vars.ParentActivity.MY_BLOG_ACTIVITY, null);
            }
        });
    }

    private void loadMyBlogs() {
        BlogController.LoadAllMyBlogs(new IAction() {
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

                myBlogRecyclerViewAdapter.setBlogs(blogs);
                myBlogRecyclerViewAdapter.notifyDataSetChanged();

            }
        });
    }

    private void switchViews(int i1, int i2) {
        myBlogMBARV.setVisibility(i1);
        noBlogMBCL.setVisibility(i2);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blog);
        RefActivity.updateACActivity(this);
        instance = new WeakReference<>(this);
        init();
        setupToolbar();
        event();
        loadMyBlogs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
