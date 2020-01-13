package com.example.test1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    boolean firstRun;
    SharedPreferences sharedPreferences;
    Intent serviceIntent;

    boolean isStepLengthValid = false;
    boolean isStepGoalValid = false;
    boolean isWeightValid = false;
    float stepLength;
    int stepGoal;
    float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P ) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACTIVITY_RECOGNITION)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed")
                            .setMessage("This permission is for tracking your steps")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
                }
            }
            else
            {
                run();
            }
        }
        else
        {
            run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                startActivity(new Intent(this, MainActivity.class));
                finish();
    }

    public void run()
    {
        sharedPreferences = this.getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        firstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if(firstRun){
            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupWithNavController(navView, navController);

            serviceIntent = new Intent(this, PedometerService.class);
            startForegroundService(serviceIntent);
        }
    }

    public void resetButton(View v) {
        sharedPreferences.edit().putInt("steps", 0).apply();
        stopService(serviceIntent);
        startForegroundService(serviceIntent);
    }

    public void stopButton(View v) {
        stopService(serviceIntent);
        finish();
    }


    public void saveButtonOnClick(View v)
    {
        final TextInputLayout layoutStepLength = findViewById(R.id.stepLengthLayoutS);
        String strStepLength = layoutStepLength.getEditText().getText().toString();

        final TextInputLayout layoutStepGoal = findViewById(R.id.stepGoalLayoutS);
        String strStepGoal = layoutStepGoal.getEditText().getText().toString();

        final TextInputLayout layoutWeight = findViewById(R.id.weightLayoutS);
        String strWeight = layoutWeight.getEditText().getText().toString();

        //Step length Validation
        if(TextUtils.isEmpty(strStepLength)){
            layoutStepLength.setErrorEnabled(true);
            layoutStepLength.setError(getString(R.string.cannotBeEmpty));
            isStepLengthValid = false;
        }
        else{

            try {
                stepLength = Float.parseFloat(strStepLength);
                layoutStepLength.setErrorEnabled(false);
                isStepLengthValid = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                layoutStepLength.setErrorEnabled(true);
                layoutStepLength.setError(getString(R.string.wrongFormat));
                isStepLengthValid = false;
            }
        }

        //Step Goal validation
        if(TextUtils.isEmpty(strStepGoal)){
            layoutStepGoal.setErrorEnabled(true);
            layoutStepGoal.setError(getString(R.string.cannotBeEmpty));
            isStepGoalValid = false;
        }
        else{

            try {
                stepGoal = Integer.parseInt(strStepGoal);
                layoutStepGoal.setErrorEnabled(false);
                isStepGoalValid = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                layoutStepGoal.setErrorEnabled(true);
                layoutStepGoal.setError(getString(R.string.wrongFormat));
                isStepGoalValid = false;
            }
        }

        //Weight Goal validation
        if(TextUtils.isEmpty(strWeight)){
            layoutWeight.setErrorEnabled(true);
            layoutWeight.setError(getString(R.string.cannotBeEmpty));
            isWeightValid = false;
        }
        else{

            try {
                weight = Float.parseFloat(strWeight);
                layoutWeight.setErrorEnabled(false);
                isWeightValid = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                layoutWeight.setErrorEnabled(true);
                layoutWeight.setError(getString(R.string.wrongFormat));
                isWeightValid = false;
            }
        }
        if(isStepGoalValid && isStepLengthValid && isWeightValid) {
            sharedPreferences = getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
            sharedPreferences.edit()
                    .putFloat("stepLength", stepLength)
                    .putInt("stepGoal", stepGoal)
                    .putBoolean("isFirstRun", false)
                    .putFloat("weight", weight)
                    .apply();
            Toast.makeText(this, getText(R.string.SettingSaved), Toast.LENGTH_LONG).show();
        }
    }

}
