package com.shangxiazuoyou.countrycodepickerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shangxiazuoyou.countrycodepicker.Country;
import com.shangxiazuoyou.countrycodepicker.PickActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPick = findViewById(R.id.tv_pick);
        tvPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, PickActivity.class), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            if (null == country) {
                return;
            }
            tvPick.setText(country.name);
        }
    }
}
