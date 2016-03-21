package com.example.alecksjohanssen.newyorktimes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Settings extends AppCompatActivity implements DialogSettings.SettingsListener {
    EditText mEditText;
    EditText xEditText;
    Button button;
    TextView tvResult;
    String reportDate;
String day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        xEditText = (EditText) findViewById(R.id.etDate);
        mEditText = (EditText) findViewById(R.id.SortChoose);
        button = (Button) findViewById(R.id.btnSelect);
        tvResult = (TextView) findViewById(R.id.tvValue);
        onClickListener();
        onButtonClickListener();
    }
    private void onClickListener() {
        xEditText.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        datePicker();
                    }

                });
    }

        private void onButtonClickListener()
    {
        button.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
             DialogSettings dialogSettings = new DialogSettings();
                dialogSettings.show(getFragmentManager(), "");
            }
        });
    }
    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(Settings.this, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.print(dateFormatter.format(newDate.getTime()));
                 reportDate = dateFormatter.format(newDate.getTime());
                xEditText.setText(new StringBuilder().append(year).append("/")
                        .append(monthOfYear +1).append("/").append(dayOfMonth));

            }
        }


                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }
    public void onSettingsSelected(String selection) {
        Toast.makeText(this, "User selected " + selection, Toast.LENGTH_SHORT).show();
        tvResult.setText(selection);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selection", selection);
        resultIntent.putExtra("date", reportDate);
        resultIntent.putExtra("sort",String.valueOf(mEditText.getText()));
        Log.d("dEbug",reportDate);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

