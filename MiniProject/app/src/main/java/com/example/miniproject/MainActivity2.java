package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private SnakeViewPanel mSnakeViewPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mSnakeViewPanel = findViewById(R.id.snake_view);

        Spinner spinner = findViewById(R.id.snakespinner);
        spinner.setSelection(1);

        Button L, Rt, T, B, S, StopBtn;

        S = findViewById(R.id.start_btn);
        L = findViewById(R.id.left_btn);
        Rt = findViewById(R.id.right_btn);
        T = findViewById(R.id.top_btn);
        B = findViewById(R.id.bottom_btn);
        StopBtn = findViewById(R.id.stopBtn);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // When an option is selected, check the position and navigate accordingly
                if (position == 0) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    // Option 2 selected, navigate to Relaxing game
                } else if (position == 2) {
                    //Option 3 selected, navigate to Meditation Video
                    Intent intent = new Intent(MainActivity2.this, MeditationVideo.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainActivity2.this, Alarm.class);
                    startActivity(intent);
                }else if (position == 4) {
                    Intent intent = new Intent(MainActivity2.this, BMI.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeViewPanel.reStartGame();
            }
        });

        L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeViewPanel.setSnakeDirection(GridSquare.GameType.LEFT);
            }
        });

        Rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeViewPanel.setSnakeDirection(GridSquare.GameType.RIGHT);
            }
        });

        T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeViewPanel.setSnakeDirection(GridSquare.GameType.TOP);
            }
        });

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnakeViewPanel.setSnakeDirection(GridSquare.GameType.BOTTOM);
            }
        });

        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnakeViewPanel.mIsEndGame = true;

            }


        });

    }

}