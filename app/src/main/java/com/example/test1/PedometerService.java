package com.example.test1;

import android.app.AlarmManager;
import android.app.Notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;



import static com.example.test1.App.CHANNEL_ID;

public class PedometerService extends Service implements SensorEventListener {

    private static SensorManager sensorManager;
    private static Sensor sensor;

    private static int steps;

    @Override
    public void onCreate() {
        super.onCreate();
        steps = getApplicationContext().getSharedPreferences("Pedometer", Context.MODE_PRIVATE).getInt("steps", 0);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        reRegisterSensor();
        updateValue();
        showNotification();

        return START_STICKY;
    }

    private void updateValue()
    {

            SharedPreferences preferences =  getApplicationContext().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
            preferences.edit().putInt("steps",steps).apply();
            showNotification();

    }

    private void showNotification()
    {
        startForeground(1, getNotification(getApplicationContext()));
    }

    public static Notification getNotification(final Context context)
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);
        SharedPreferences preferences = context.getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        int stepsFromShared = preferences.getInt("steps", 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.NotifTitle))
                .setContentText(String.valueOf(stepsFromShared) + context.getString(R.string.NotifSteps) + " ")
                .setSmallIcon(R.drawable.ic_directions_walk_black_24dp)
                .setContentIntent(pendingIntent).build();
        return notification;
    }


    private void reRegisterSensor()
    {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }
        else
        {
            Toast.makeText(this, "Sensor is not Present", Toast.LENGTH_LONG).show();
        }

        try {
            sensorManager.unregisterListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        steps =  preferences.getInt("steps",0)+1;
        updateValue();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
