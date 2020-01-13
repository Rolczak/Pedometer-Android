package com.example.test1.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.test1.R;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment extends Fragment {
    private TextInputLayout layoutStepLength;
    private TextInputLayout layoutStepGoal ;
    private TextInputLayout layoutWeight;

    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        layoutStepLength = root.findViewById(R.id.stepLengthLayoutS);
        layoutStepGoal= root.findViewById(R.id.stepGoalLayoutS);
        layoutWeight = root.findViewById(R.id.weightLayoutS);
        sharedPreferences = getActivity().getSharedPreferences("Pedometer", Context.MODE_PRIVATE);
        setTextEditsValues();
        return root;
    }

    private void setTextEditsValues()
    {
        layoutStepGoal.getEditText().setText(String.valueOf(sharedPreferences.getInt("stepGoal",0)));
        layoutStepLength.getEditText().setText(String.valueOf(sharedPreferences.getFloat("stepLength", 0)));
        layoutWeight.getEditText().setText(String.valueOf(sharedPreferences.getFloat("weight", 0)));
    }


}