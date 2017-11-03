package com.example.java_oglen.urunyonetimi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class UrunList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_list);

        String katid = getIntent().getExtras().getString("kid");
        Log.d("kid", katid);
    }
}
