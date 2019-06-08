package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPayClick(View v)
    {
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Payworks Pay");
    }

    public void buttonRefundClick(View v)
    {
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Payworks Refund");
    }
}
