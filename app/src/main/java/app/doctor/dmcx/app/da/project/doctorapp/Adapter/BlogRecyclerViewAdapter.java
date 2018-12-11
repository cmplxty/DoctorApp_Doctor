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
import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.BlogRecyclerViewHolder> {

    private List<Blog> blogs = new ArrayList<>();

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_blog, parent, false);
        return new BlogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        if (!blogs.get(position).getImage_link().equals(""))
            Picasso.with(RefActivity.refACActivity.get())
                    .load(blogs.get(position).getImage_link())
                    .placeholder(R.drawable.noperson)
                    .into(holder.bloggerProfileImageBCIV);

        if (!blogs.get(position).getPoster().equals(""))
            Picasso.with(RefActivity.refACActivity.get())
                    .load(blogs.get(position).getPoster())
                    .placeholder(R.drawable.no_image_available)
                    .into(holder.blogPosterBIV, PosterImageCallback.getInstance().setImageView(holder.blogPosterBIV));

        holder.blogWriterNameBTV.setText(blogs.get(position).getName());
        holder.dateBTV.setText(blogs.get(position).getDate());
        holder.blogWriterDetailBIV.setText(blogs.get(position).getDetail());
        holder.blogTitleBTV.setText(blogs.get(position).getTitle());
        holder.blogContentBTV.setText(blogs.get(position).getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BlogViewerActivity(Vars.ParentActivity.HOME_ACTIVITY, blogs.get(itemPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView bloggerProfileImageBCIV;
        private TextView blogWriterNameBTV;
        private TextView dateBTV;
        private TextView blogWriterDetailBIV;
        private ImageView blogPosterBIV;
        private TextView blogTitleBTV;
        private TextView blogContentBTV;

        BlogRecyclerViewHolder(View itemView) {
            super(itemView);

            bloggerProfileImageBCIV = itemView.findViewById(R.id.bloggerProfileImageBCIV);
            blogWriterNameBTV = itemView.findViewById(R.id.blogWriterNameBTV);
            dateBTV = itemView.findViewById(R.id.dateBTV);
            blogWriterDetailBIV = itemView.findViewById(R.id.blogWriterDetailBIV);
            blogPosterBIV = itemView.findViewById(R.id.blogPosterBIV);
            blogTitleBTV = itemView.findViewById(R.id.blogTitleMBTV);
            blogContentBTV = itemView.findViewById(R.id.blogContentBTV);
        }
    }
}
