package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Switch;
import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;

public class PrefsFragment extends PreferenceFragment {

    private Preference onHomeServiceClickKey;
    private Preference onNewAssistantClickKey;
    private Preference onAppointmentClickKey;
    private Preference onAudioCallClickKey;

    private void init() {
        onHomeServiceClickKey = findPreference("onHomeServiceClickKey");
        onAppointmentClickKey = findPreference("onAppointmentClickKey");
        onNewAssistantClickKey = findPreference("onNewAssistantClickKey");
        onAudioCallClickKey = findPreference("onAudioCallClickKey");
    }

    private void preferences() {
        onAppointmentClickKey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog checkingDialog = LoadingDialog.on(LoadingText.Checking);
                AppointmentController.CheckAppointmentDoctor(new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        LoadingDialog.off(checkingDialog);
                        triggerAppointment((Boolean) object);
                    }
                });
                return true;
            }
        });

        onNewAssistantClickKey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ActivityTrigger.CreateNewAssistantActivity();
                return true;
            }
        });

        onAudioCallClickKey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog checkingDialog = LoadingDialog.on(LoadingText.Checking);
                AudioCallController.CheckAudioCallStatus(new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        LoadingDialog.off(checkingDialog);
                        triggerAudioCall((String) object);
                    }
                });
                return true;
            }
        });

        onHomeServiceClickKey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                HomeServiceController.CheckForRegisteredHomeServiceDoctor(new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        triggerHomeService((Boolean) object);
                    }
                });

                return true;
            }
        });
    }

    private void triggerAppointment(boolean isSuccess) {
        if (!isSuccess)
            ActivityTrigger.SetupAppointmentActivity();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
            builder.setTitle("Notice");
            builder.setMessage("Remove as appointment doctor?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    AppointmentController.RemoveAppointmentDoctor();
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

    private void triggerAudioCall(final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
        builder.setTitle("Notice");
        if (status.equals(AFModel.online)) {
            builder.setMessage("You are online. You want to go offline?");
        } else {
            builder.setMessage("You are offline. You want to go online?");
        }

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (status.equals(AFModel.online)) {
                    AudioCallController.SetAudioCallDoctor(AFModel.offline);
                } else {
                    AudioCallController.SetAudioCallDoctor(AFModel.online);
                }

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

    private void triggerHomeService(boolean isSuccess) {
        if (!isSuccess)
            ProfileController.CheckForProfileData(new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if (object != null) {
                        ActivityTrigger.HomeServiceRegisterActivity((Doctor) object);
                    }
                }
            });
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
            builder.setTitle("Notice");
            builder.setMessage("Unregister as home service doctor?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    HomeServiceController.UnregisterHomeService();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.home_fragment_preference);

        init();
        preferences();
    }
}
