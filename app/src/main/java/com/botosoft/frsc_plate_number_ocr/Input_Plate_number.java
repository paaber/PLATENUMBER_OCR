package com.botosoft.frsc_plate_number_ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Input_Plate_number extends AppCompatActivity {

    EditText inputPlateNumber;
    ImageButton submit;
    private JSONObject jsonObject;
    private String[] sError;
    String confidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input__plate_number);

        inputPlateNumber = findViewById(R.id.inputplatenumberETInputPlateNumber);
        submit = findViewById(R.id.submitButtonInputPlateNumber);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPlateNumber();

            }
        });
    }

    public void verifyPlateNumber() {
        String scannedPlateNumber = inputPlateNumber.getText().toString();

        Intent in = new Intent(Input_Plate_number.this, Drivers_Details.class);
        in.putExtra("plateNumber", scannedPlateNumber);
//                                in.putExtra("carColor",carColor);
//                                in.putExtra("carModel",carModel);
        startActivity(in);


    }
}
