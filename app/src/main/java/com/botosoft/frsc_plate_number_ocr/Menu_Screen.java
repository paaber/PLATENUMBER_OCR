package com.botosoft.frsc_plate_number_ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__screen);


        ImageButton scanPlateNumber = findViewById(R.id.scanplatenumber);
        ImageButton inputPlateNumber = findViewById(R.id.inputplatenumber);


        inputPlateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intn = new Intent(Menu_Screen.this,Input_Plate_number.class);
                startActivity(intn);
            }
        });

        scanPlateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intn = new Intent(Menu_Screen.this,Scan_Plate_Number.class);
                startActivity(intn);
            }
        });


    }
}
