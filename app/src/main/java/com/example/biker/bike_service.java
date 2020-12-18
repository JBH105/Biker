package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class bike_service extends AppCompatActivity {
    Spinner spinner;
    TextInputEditText plate_number,remark;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_service);

        spinner=findViewById(R.id.spinner);
        plate_number=findViewById(R.id.plate_number);
        remark=findViewById(R.id.remark);
        next=findViewById(R.id.next);

        //spinner
        ArrayAdapter<CharSequence> Adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.bike,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = plate_number.getText().toString().trim();
                String Remark = remark.getText().toString().trim();
                String Spinner = spinner.getSelectedItem().toString().trim();

                if (Spinner.equals("Selecte Model")){
                    Toast.makeText(bike_service.this, "select model", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.isEmpty()){
                    plate_number.setError("Enter plate number");
                    return;
                }
                if (Remark.isEmpty()){
                    remark.setError("Enter Remark");
                    return;
                }
                startActivity(new Intent(getApplicationContext(), bike_service_location.class));
            }
        });

    }
}