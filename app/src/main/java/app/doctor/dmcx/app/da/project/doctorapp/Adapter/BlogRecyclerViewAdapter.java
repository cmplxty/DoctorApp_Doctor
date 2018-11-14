package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.BlogRecyclerViewHolder> {

    @NonNull
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_blog, parent, false);
        return new BlogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        BlogRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
