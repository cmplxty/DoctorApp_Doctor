package app.doctor.dmcx.app.da.project.doctorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.ListUpdateCallback;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.EmailAuthProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AuthController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AuthActivity extends AppCompatActivity {

    // Variables
    private Button signInBTN;
    private EditText emailSiET;
    private EditText passwordSiET;
    private TextView forgetPasswordTV;
    // Variables

    // Methods
    private void init() {
        signInBTN = findViewById(R.id.signInBTN);
        emailSiET = findViewById(R.id.emailSiET);
        passwordSiET = findViewById(R.id.passwordSiET);
        forgetPasswordTV = findViewById(R.id.forgetPasswordTV);

        Vars.appFirebase = new AppFirebase();
    }

    private void events() {
        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthController.SignIn(getEmailAndPassword().get(0), getEmailAndPassword().get(1),
                        new IAction() {
                            @Override
                            public void onCompleteAction(Object object) {
                                if (object instanceof Boolean) {
                                    Boolean isSuccessful = (Boolean) object;
                                    if (isSuccessful) {
                                        startHomeActivity();
                                    } else {
                                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AuthenticationFailed, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (object instanceof String) {
                                    String errorCode = (String) object;
                                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    private List<String> getEmailAndPassword() {
        List<String> auth = new ArrayList<>();
        auth.add(emailSiET.getText().toString());
        auth.add(passwordSiET.getText().toString());

        return auth;
    }

    /*
     * Start main activity
     * */
    private void startHomeActivity() {
        startActivity(new Intent(AuthActivity.this, HomeActivity.class));
        finish();
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        RefActivity.updateACActivity(this);

        init();
        events();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AuthController.isUserLoggedIn()) {
            startHomeActivity();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
