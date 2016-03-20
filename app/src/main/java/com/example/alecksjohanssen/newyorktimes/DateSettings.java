package com.example.alecksjohanssen.newyorktimes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by My name Is Hardline on 2/26/2016.
 */
public class DateSettings implements DatePickerDialog.OnDateSetListener {
    Context context;
    TextView mEditText;


    public DateSettings(Context context) {

        this.context = context;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthofyear,int dayofmonth) {
        Toast.makeText(context, "Selected date" + monthofyear + "/" + dayofmonth + "/" + year, Toast.LENGTH_LONG).show();
    }

}

