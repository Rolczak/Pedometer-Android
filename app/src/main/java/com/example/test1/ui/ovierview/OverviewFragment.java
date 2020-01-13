package com.example.test1.ui.ovierview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.test1.R;
import com.github.lzyzsd.circleprogress.CircleProgress;

public class OverviewFragment extends Fragment {

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private TextView stepsTextView;
    private TextView stepsToGoal;
    private TextView distance;
    private TextView calories;

    private CircleProgress circleProgress;
    private int stepGoal;
    private float stepLength;
    private int steps;
    private float weight;

    private void getVariables(){
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        steps = sharedPreferences.getInt("steps",0);
        stepsTextView.setText(String.valueOf(steps));
        stepGoal = sharedPreferences.getInt("stepGoal", 100);
        stepLength = sharedPreferences.getFloat("stepLength", 65);
        weight = sharedPreferences.getFloat("weight", 50);
    }

    private void updateInformation(){

        if(steps >= stepGoal){
            circleProgress.setProgress(100);
            stepsToGoal.setText(" " +getText(R.string.completeGoal));
        }
        else{
            circleProgress.setProgress(steps*100/stepGoal);
            stepsToGoal.setText(String.valueOf(stepGoal-steps));
        }

        float dist = (steps*stepLength)/100;
        if(dist > 1000)        {
            dist /= 1000;
            distance.setText(String.valueOf(dist)+" km");
        }
        else{
            distance.setText(String.format("%.2f", dist)+" m");
        }

        int cal =(int) ((weight * 0.708) * ((steps *stepLength)/100000));
        calories.setText(String.valueOf(cal)+" kCal");

    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        stepsTextView = root.findViewById(R.id.stepsTextView);
        circleProgress = root.findViewById(R.id.circle_progress);
        stepsToGoal = root.findViewById(R.id.stepsToGoal);
        distance = root.findViewById(R.id.distanceTextView);
        calories = root.findViewById(R.id.caloriesTextView);
        getVariables();
        updateInformation();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals("steps"))
                {
                    stepsTextView.setText(String.valueOf(prefs.getInt("steps",0)));
                    steps = prefs.getInt("steps",0);
                    updateInformation();
                }

            }
        };
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        updateInformation();

    }
}