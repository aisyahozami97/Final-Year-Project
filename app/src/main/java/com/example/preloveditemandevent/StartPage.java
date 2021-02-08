package com.example.preloveditemandevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        getSupportActionBar().setTitle("Preloved Sales");
    }

    public void btn_getStart(View view) {

        startActivity(new Intent(getApplicationContext(), Login_Form.class));
    }
}
