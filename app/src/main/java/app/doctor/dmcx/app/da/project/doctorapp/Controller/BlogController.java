package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
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

}
