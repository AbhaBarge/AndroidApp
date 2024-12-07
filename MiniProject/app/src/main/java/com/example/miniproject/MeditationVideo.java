package com.example.miniproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MeditationVideo extends AppCompatActivity {

    Spinner spinner;
    MediaPlayer pl1, pl2, pl3, pl4;

    Button s1pl, s1pa, s2pl, s2pa, s3pl, s3pa, s4pl, s4pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.myspinner);
        spinner.setSelection(2);

        s1pl = findViewById(R.id.btnPlay);
        s1pa = findViewById(R.id.btnPause);

        s2pa = findViewById(R.id.btnPlay2);
        s2pl = findViewById(R.id.btnPause2);

        s3pa = findViewById(R.id.btnPlay3);
        s3pl = findViewById(R.id.btnPause3);

        s4pa = findViewById(R.id.btnPlay4);
        s4pl = findViewById(R.id.btnPause4);

        pl1 = MediaPlayer.create(getApplicationContext(), R.raw.calm_waters);
        pl2 = MediaPlayer.create(getApplicationContext(), R.raw.silent_skies);
        pl3 = MediaPlayer.create(getApplicationContext(), R.raw.warm_woods);
        pl4 = MediaPlayer.create(getApplicationContext(), R.raw.serene);

        pl1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("MediaPlayerError", "Error: what=" + what + ", extra=" + extra);
                // Handle reset or release here
                return true; // Return true if error was handled
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // When an option is selected, check the position and navigate accordingly
                if (position == 0) {
                    // Option 1 selected, navigate to FirstActivity
                    Intent intent = new Intent(MeditationVideo.this, MainActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    // Option 2 selected, navigate to Relaxing game
                    Intent intent = new Intent(MeditationVideo.this, MainActivity2.class);
                    startActivity(intent);
                } else if (position == 2) {
                    //Option 3 selected, navigate to Meditation Video

                } else if (position == 3) {
                    Intent intent = new Intent(MeditationVideo.this, Alarm.class);
                    startActivity(intent);
                }
                else if(position == 4)
                {
                    Intent intent = new Intent(MeditationVideo.this, BMI.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        s1pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
                pl1.start();
            }
        });

        s2pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
                pl2.start();
            }
        });

        s3pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
                pl3.start();
            }
        });

        s4pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
                pl4.start();
            }
        });

        s1pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
            }
        });
        s2pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
            }
        });
        s3pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
            }
        });
        s4pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAll();
            }
        });

    }

    private void pauseAll()
    {
        if (pl1 != null && pl1.isPlaying()) {
            pl1.pause();
        }
        if (pl2 != null && pl2.isPlaying()) {
            pl2.pause();
        }
        if (pl3 != null && pl3.isPlaying()) {
            pl3.pause();
        }
        if (pl4 != null && pl4.isPlaying()) {
            pl4.pause();
        }
    }
}