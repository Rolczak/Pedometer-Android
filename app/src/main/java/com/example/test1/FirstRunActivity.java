package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class FirstRunActivity extends AppCompatActivity {

    boolean isStepLengthValid = false;
    boolean isStepGoalValid = false;
    boolean isWeightValid = false;

    float stepLength;
    int stepGoal;
    float weight;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

    }

    public void saveButtonOnClick(View view)    {
        final TextInputLayout layoutStepLength = findViewById(R.id.stepLengthLayout);
        String strStepLength = layoutStepLength.getEditText().getText().toString();

        final TextInputLayout layoutStepGoal = findViewById(R.id.stepGoalLayout);
        String strStepGoal = layoutStepGoal.getEditText().getText().toString();

        final TextInputLayout layoutWeight = findViewById(R.id.weightLayout);
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
        if(isStepGoalValid && isStepLengthValid && isWeightValid)        {
            preferences = getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
            preferences.edit()
                    .putFloat("stepLength", stepLength)
                    .putInt("stepGoal", stepGoal)
                    .putBoolean("isFirstRun", false)
                    .putFloat("weight", weight )
                    .apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
