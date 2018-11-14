package app.doctor.dmcx.app.da.project.doctorapp.Activities.Blog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import id.zelory.compressor.Compressor;

public class BlogEditorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView blogImageBEIV;
    private EditText blogTitleBEET;
    private EditText blogContentBEET;

    private String activityAction;
    private Blog blog;
    private Uri blogImage;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        blogImageBEIV = findViewById(R.id.blogImageBEIV);
        blogTitleBEET = findViewById(R.id.blogTitleBEET);
        blogContentBEET = findViewById(R.id.blogContentBEET);

        blog = getIntent().getParcelableExtra(Vars.Connector.BLOG_FRAGMENT_DATA);
        if (blog != null) {
            if (!blog.getImage_link().equals(""))
                Picasso.with(RefActivity.refACActivity.get()).load(blog.getImage_link()).placeholder(R.drawable.wallpaper_health).into(blogImageBEIV);
            blogTitleBEET.setText(blog.getTitle());
            blogContentBEET.setText(blog.getContent());
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void event() {
        blogImageBEIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    cropIamger();
                }
            }
        });
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(RefActivity.refACActivity.get(), new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_BEA);
            return false;
        }

        return true;
    }

    private void cropIamger() {
        CropImage.activity()
                .setAspectRatio(16, 9)
                .start(RefActivity.refACActivity.get());
    }

    private Uri compressImage(Uri uri) {
        File actualFile = new File(uri.getPath());
        try {
            File compressedFile = new Compressor(RefActivity.refACActivity.get())
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(actualFile);

            return Uri.fromFile(compressedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_editor);
        RefActivity.updateACActivity(this);
        init();
        setupToolbar();
        event();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveBEMI:
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_BEA) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
                builder.setTitle("Permission")
                        .setCancelable(false)
                        .setMessage("Need permission to get the image from the storage. We don't access any of your private data. Be safe stay safe.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                cropIamger();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result != null) {
                blogImage = result.getUri();
                Uri compress = compressImage(blogImage);

                if (compress == null)
                    blogImageBEIV.setImageURI(blogImage);
                else {
                    Toast.makeText(RefActivity.refACActivity.get(), "Compressed", Toast.LENGTH_SHORT).show();
                    blogImageBEIV.setImageURI(compress);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                    Toast.makeText(RefActivity.refACActivity.get(), "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
