package com.example.miniproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.*;

public class BMI extends AppCompatActivity {

    float h, w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btncalc, btnrecalc;
        btncalc = findViewById(R.id.calcbtn);
        btnrecalc = findViewById(R.id.recalc);
        btnrecalc.setVisibility(View.GONE);
        TextView t1 = (TextView) findViewById(R.id.ht);
        TextView t2 = (TextView) findViewById(R.id.wt);
        TextView res = (TextView) findViewById(R.id.result);
        btncalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("" + t1.getText()).isEmpty() && !("" + t2.getText()).isEmpty()) {

                    h = Float.parseFloat("" + t1.getText()) / 100;
                    w = Float.parseFloat("" + t2.getText());
                    res.setText("With BMI = " + (w / (h * h)) + "\n You are: " + this.getres(h, w));
                    AlertDialog.Builder builder = new AlertDialog.Builder(BMI.this);
                    builder.setTitle("Your BMI is = "+ (w / (h * h)));
                    builder.setItems(new String[]{"Okay"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    builder.show();

                    this.chgbtn();
                } else if (("" + t1.getText()).isEmpty() && ("" + t2.getText()).isEmpty()) {
                    Toast.makeText(BMI.this, "Enter valid height and weight values !", Toast.LENGTH_LONG).show();

                } else if (("" + t1.getText()).isEmpty()) {
                    Toast.makeText(BMI.this, "Enter a valid height value !", Toast.LENGTH_LONG).show();

                } else if (("" + t2.getText()).isEmpty()) {
                    Toast.makeText(BMI.this, "Enter a valid weight value !", Toast.LENGTH_LONG).show();

                }
            }

            private void chgbtn() {
                btncalc.setVisibility(View.GONE);
                btnrecalc.setVisibility(View.VISIBLE);
                btnrecalc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        this.resetEverything();
                    }

                    void resetEverything() {
                        t1.setText("");
                        t2.setText("");
                        res.setText("");
                        btnrecalc.setVisibility(View.GONE);
                        btncalc.setVisibility(View.VISIBLE);
                    }
                });
            }

            private String getres(float h, float w) {
                String s = "";
                float bmi = w / (h * h);
                if (bmi < 18.5)
                    s = "Underweight";
                else if (bmi < 25)
                    s = "Normal weighted";
                else if (bmi < 40)
                    s = "Overweight";
                else
                    s = "Obese";

                return s;

            }
        });

        Spinner spinner = findViewById(R.id.bmispinner);

        spinner.setSelection(4);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // When an option is selected, check the position and navigate accordingly
                if (position == 0) {
                    // Option 1 selected, navigate to FirstActivity
                    Intent intent = new Intent(BMI.this, MainActivity.class);
                    startActivity(intent);

                } else if (position == 1) {
                    // Option 2 selected, navigate to Relaxing game
                    Intent intent = new Intent(BMI.this, MainActivity2.class);
                    startActivity(intent);
                } else if (position == 2) {
                    //Option 3 selected, navigate to Meditation Video
                    Intent intent = new Intent(BMI.this, MeditationVideo.class);
                    startActivity(intent);
                }
                else if(position == 3)
                {
                    Intent intent = new Intent(BMI.this, Alarm.class);
                    startActivity(intent);
                }

                else if(position==4)
                {
                    //BMI
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }
}