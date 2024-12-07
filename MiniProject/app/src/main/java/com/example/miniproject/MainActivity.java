package com.example.miniproject;

import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    Bitmap bg = Bitmap.createBitmap(720, 1200, Bitmap.Config.ARGB_8888);


    ImageView iv = (ImageView) findViewById(R.id.imageView);
    iv.setBackground(new BitmapDrawable(bg));

// Creating background using graphics
    Canvas canvas = new Canvas(bg);
    Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setColor(Color.parseColor("#FFBF00")); // Amber
        paint.setStyle(Paint.Style.FILL);

        float radius = 200 ;
        float x = 0;
        float y = 0;
        canvas.drawCircle(x, y, radius, paint);
        canvas.drawLine(radius, y+50, radius+50, y+50, paint);
        int numRays = 30;
        float angle = 0;
        for (int i = 0; i < numRays; i++) {
            float startX = x +2+ radius * (float) Math.sin(angle);
            float startY = y +2+ radius * (float) Math.cos(angle);
            float endX = x + (radius + 50) * (float) Math.sin(angle);
            float endY = y + (radius + 50) * (float) Math.cos(angle);
            angle+=10;
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        radius = 70;
        x = 120;
        y = 400;
        canvas.drawCircle(x, y, radius, paint);

        radius = 57;
        x = 585;
        y = 1000;
        canvas.drawCircle(x, y, radius, paint);

        radius = 92;
        x = 215;
        y = 1100;
        canvas.drawCircle(x, y, radius, paint);

        radius = 100;
        x = 575;
        y = 250;
        canvas.drawCircle(x, y, radius, paint);

        radius = 28;
        x = 476;
        y = 35;
        canvas.drawCircle(x, y, radius, paint);

        int centerX = 320;
        int centerY = 850;

        // Draw face
        paint.setColor(Color.parseColor("#FFBF00"));
        canvas.drawCircle(centerX, centerY, 150, paint); // Face

        // Draw eyes
        paint.setColor(Color.BLACK);
        //canvas.drawCircle(centerX - 60, centerY - 40, 20, paint); // Left eye
        //canvas.drawCircle(centerX + 60, centerY - 40, 20, paint); // Right eye

        // Draw mouth
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawArc(centerX - 100, centerY - 50, centerX + 100, centerY + 100, 0, 180, false, paint); // Smile

        canvas.drawArc(centerX - 115, centerY-15 , centerX - 90, centerY + 25, 0, -180, false, paint); // Smile
        canvas.drawArc(centerX + 90, centerY-15 , centerX + 115, centerY + 25, 0, -180, false, paint); // Smile


        // Main Menu
        Spinner mySpinner = findViewById(R.id.my_spinner);

        // Create an ArrayAdapter using a simple spinner layout and your string array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // Option 1 selected, stay

                } else if (position == 1) {
                    // Option 2 selected, navigate to Relaxing game
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                } else if (position == 2) {
                    //Option 3 selected, navigate to Meditation Video
                    Intent intent = new Intent(MainActivity.this, MeditationVideo.class);
                    startActivity(intent);
                }
                else if(position == 3)
                {
                    Intent intent = new Intent(MainActivity.this, Alarm.class);
                    startActivity(intent);
                }

                else if(position==4)
                {
                    Intent intent = new Intent(MainActivity.this, BMI.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}



