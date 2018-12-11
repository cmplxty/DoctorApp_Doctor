package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.PosterImageCallback;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MyBlogRecyclerViewAdapter extends RecyclerView.Adapter<MyBlogRecyclerViewAdapter.BlogRecyclerViewHolder> {

    private List<Blog> blogs = new ArrayList<>();

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_my_blog, parent, false);
        return new BlogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        if (!blogs.get(position).getPoster().equals(""))
            Picasso.with(RefActivity.refACActivity.get())
                    .load(blogs.get(position).getPoster())
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.blogPosterMBIV, PosterImageCallback.getInstance().setImageView(holder.blogPosterMBIV));

        holder.dateMBTV.setText(blogs.get(position).getDate());
        holder.blogTitleMBTV.setText(blogs.get(position).getTitle());
        holder.blogContentMBTV.setText(blogs.get(position).getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BlogEditorActivity(Vars.ParentActivity.MY_BLOG_ACTIVITY, blogs.get(itemPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView blogPosterMBIV;
        private TextView dateMBTV;
        private TextView blogTitleMBTV;
        private TextView blogContentMBTV;

        BlogRecyclerViewHolder(View itemView) {
            super(itemView);

            dateMBTV = itemView.findViewById(R.id.dateMBTV);
            blogPosterMBIV = itemView.findViewById(R.id.blogPosterMBIV);
            blogTitleMBTV = itemView.findViewById(R.id.blogTitleMBTV);
            blogContentMBTV = itemView.findViewById(R.id.blogContentMBTV);
        }
    }
}
