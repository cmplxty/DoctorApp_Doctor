package app.doctor.dmcx.app.da.project.doctorapp.Module;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import app.doctor.dmcx.app.da.project.doctorapp.R;

public class CustomPreferenceCategory extends PreferenceCategory {

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView titleTV = view.findViewById(android.R.id.title);
        titleTV.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
    }
}
