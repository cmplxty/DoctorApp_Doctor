package app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView showImageIV = findViewById(R.id.showImageIV);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(showImageIV);
        photoViewAttacher.update();

        String url = getIntent().getStringExtra(Vars.Connector.VIEW_IMAGE_DATA);
        Picasso.with(RefActivity.refACActivity.get()).load(url).placeholder(R.drawable.nophoto).into(showImageIV);
    }
}
