package com.bmnhanuoc.wirelesslock20.topfrag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bmnhanuoc.wirelesslock20.R;

public class HelpsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helps);
        textView = findViewById(R.id.text_help);
        textView.setText("1. You need to input IP \n" +
                "2. Then login \n" +
                "3. To delete: click on user's image to delete that user");
    }
}