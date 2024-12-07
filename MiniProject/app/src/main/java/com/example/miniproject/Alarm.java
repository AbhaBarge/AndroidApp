package com.example.miniproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TimePickerDialog timePickerDialog;
    int hour, minute;
    Button stopbtn;
    ToggleButton tg;

    String selSong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        Button btn = findViewById(R.id.button);
        Button btnSong = findViewById(R.id.button2);
        TextView alarm_time = findViewById(R.id.textView3);

        String[] songs = {"Jingle Bells", "Ring-A-Tring", "Close"};
        MediaPlayer pl0 = MediaPlayer.create(getApplicationContext(), R.raw.jinglebells);
        MediaPlayer pl1 = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a song");
        builder.setItems(songs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    if (pl1.isPlaying())
                        pl1.pause();
                    builder.show();
                    if (pl0 != null)
                        pl0.start();
                }
                else if (which == 1)
                {
                    if (pl0.isPlaying())
                        pl0.pause();
                    builder.show();
                    if (pl1 != null)
                        pl1.start();
                }
                else {
                    if (pl0.isPlaying()) {
                        selSong = "jm";
                        pl0.pause();
                    }
                    if (pl1.isPlaying()) {
                        selSong = "rt";
                        pl1.pause();
                    }
                }
            }
        });


        Spinner spinner = findViewById(R.id.alarmspinner);

        String[] menu = {"Home", "Relaxing Game", "Meditation Songs", "Productivity Alarm", "Calcul8 my We8"};

        ArrayAdapter<CharSequence> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,menu);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        spinner.setSelection(3);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // When an option is selected, check the position and navigate accordingly
                if (position == 0) {
                    // Option 1 selected, navigate to FirstActivity
                    Intent intent = new Intent(Alarm.this, MainActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    // Option 2 selected, navigate to Relaxing game
                    Intent intent = new Intent(Alarm.this, MainActivity2.class);
                    startActivity(intent);
                } else if (position == 2) {
                    //Option 3 selected, navigate to Meditation Video
                    Intent intent = new Intent(Alarm.this, MeditationVideo.class);
                    startActivity(intent);

                } else if (position == 3) {
                    //alarm
                } else if (position == 4) {
                    Intent intent = new Intent(Alarm.this, BMI.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        tg = findViewById(R.id.toggleButton);
        stopbtn = findViewById(R.id.StopAlarm);

        btn.setOnClickListener(view -> {
            timePickerDialog = new TimePickerDialog(Alarm.this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                    hour = hourOfDay;
                    minute = min;
                    String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                    alarm_time.setText(String.format("%02d:%02d %s", hourOfDay % 12, minute, am_pm));
                }
            }, hour, minute, false);
            timePickerDialog.show();
            tg.setVisibility(View.VISIBLE);
            stopbtn.setVisibility(View.VISIBLE);
        });

        btnSong.setOnClickListener(view -> builder.show());

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver al = new AlarmReceiver();
                al.stopAlarm();
                Toast.makeText(Alarm.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
                Log.d("Alarm", "Alarm stopped");
                tg.setChecked(false);
                Alarm.this.OnToggleClicked(tg);
            }
        });
    }

    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(Alarm.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            time = calendar.getTimeInMillis();

            Intent intent = new Intent(this, AlarmReceiver.class);

            intent.putExtra("Selected", selSong);

            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


            if (System.currentTimeMillis() > time) {
                // If the set time has already passed today, set for tomorrow
                time += AlarmManager.INTERVAL_DAY;
                Toast.makeText(Alarm.this, "Alarm set for tmr", Toast.LENGTH_LONG).show();

            }

            Log.d("Alarm", "Alarm set for: " + calendar.getTime());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }

        }
        else {
            if (alarmManager != null && pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
            }
            AlarmReceiver al = new AlarmReceiver();
            al.stopAlarm();
            Toast.makeText(Alarm.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
            Log.d("Alarm", "Alarm cancelled");
            tg.setVisibility(View.INVISIBLE);
            stopbtn.setVisibility(View.INVISIBLE);
        }


    }
}
