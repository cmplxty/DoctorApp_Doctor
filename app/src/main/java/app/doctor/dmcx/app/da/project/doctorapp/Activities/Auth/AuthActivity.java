package app.doctor.dmcx.app.da.project.doctorapp.Activities.Auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AuthController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
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

    // Class
    public class AuthDialog {
        public void create() {
            int px16 = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
            int px14 = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 0, 0);

            final EditText emailET = new EditText(RefActivity.refACActivity.get());
            emailET.setHint("Enter your email...");
            emailET.setPadding(px16+px14, px16, px16+px14, px16);
            emailET.setLayoutParams(params);
            emailET.setBackground(getResources().getDrawable(R.drawable.edit_text_clear_bg, null));

            AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
            builder.setTitle("Enter your email");
            builder.setView(emailET);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String email = emailET.getText().toString();
                    if (!email.equals(""))
                        AuthController.ForgetPassword(email.trim());
                    else
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EnterEmail, Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1EC8C8"));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1EC8C8"));
                }
            });
            dialog.show();

        }
    }

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

        forgetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthDialog dialog = new AuthDialog();
                dialog.create();
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
