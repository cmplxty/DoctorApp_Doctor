package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.net.Uri;
import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class BlogController {

    public static void LoadLastBlog(final IAction action) {
        Vars.appFirebase.loadLastBlog(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void LoadAllBlogs(final IAction action) {
        Vars.appFirebase.loadAllBlogs(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void LoadAllMyBlogs(final IAction action) {
        Vars.appFirebase.loadAllMyBlogs(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void UpdateBlog(Uri poster, final Blog blog) {
        if (poster != null) {
            SavePoster(poster, new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if (object != null) {
                        UpdateBlog(blog.getId(), String.valueOf(object), blog.getTitle(), blog.getContent());
                    } else {
                        UpdateBlog(blog.getId(), blog.getPoster(), blog.getTitle(), blog.getContent());
                    }
                }
            });
        } else {
            UpdateBlog(blog.getId(), blog.getPoster(), blog.getTitle(), blog.getContent());
        }
    }

    private static void UpdateBlog(String blogId, String posterLink, String title, String content) {
        Doctor doctor = ProfileController.GetLocalProfile();
        if (doctor != null) {
            Vars.appFirebase.updateBlog(blogId, doctor, posterLink, title, content, new ICallback() {
                @Override
                public void onCallback(boolean isSuccessful, Object object) {
                    if (isSuccessful) {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.BlogUpdated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToUpdateBlog, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            LoadProfileAndSaveBlog(posterLink, title, content);
            Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ProfileDataNotAvailable, Toast.LENGTH_SHORT).show();
        }
    }

    public static void SaveBlog(Uri poster, final Blog blog) {
        if (poster != null) {
            SavePoster(poster, new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if (object != null) {
                        SaveBlog(String.valueOf(object), blog.getTitle(), blog.getContent());
                    } else {
                        SaveBlog(blog.getPoster(), blog.getTitle(), blog.getContent());
                    }
                }
            });
        } else {
            SaveBlog(blog.getPoster(), blog.getTitle(), blog.getContent());
        }
    }

    private static void SavePoster(Uri poster, final IAction action) {
        Vars.appFirebase.saveBlogPoster(poster, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (!isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToSaveBlogPoster, Toast.LENGTH_SHORT).show();
                }

                action.onCompleteAction(object);
            }
        });
    }

    private static void SaveBlog(String posterLink, String title, String content) {
        Doctor doctor = ProfileController.GetLocalProfile();
        if (doctor != null) {
            Vars.appFirebase.saveBlog(doctor, posterLink, title, content, new ICallback() {
                @Override
                public void onCallback(boolean isSuccessful, Object object) {
                    if (isSuccessful) {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.BlogAdded, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToAddBlog, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            LoadProfileAndSaveBlog(posterLink, title, content);
            Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ProfileDataNotAvailable, Toast.LENGTH_SHORT).show();
        }
    }

    private static void LoadProfileAndSaveBlog(final String posterLink, final String title, final String content) {

        ProfileController.LoadProfile(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null)
                    SaveBlog(posterLink, title, content);
            }
        });

    }

}
