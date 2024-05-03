package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.assessment2restaurantapp.R;

public class AccountInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public AccountInfoFragment() {
        // Required empty public constructor
    }

    public static AccountInfoFragment newInstance(String param1, String param2) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        setDefaultMealTime(view);
        setDefaultSeatingArea(view);
        setDefaultNotifications(view);


        return view;
    }


    private void setDefaultMealTime(View view) {

        RadioButton radioLunch = view.findViewById(R.id.radioLunch);
        radioLunch.setChecked(true);
    }
    private void setDefaultSeatingArea(View view) {
        RadioButton radioGarden = view.findViewById(R.id.radioGarden);
        radioGarden.setChecked(true);
    }

    private void setDefaultNotifications(View view) {
        CheckBox checkAllow = view.findViewById(R.id.checkAllow);
        checkAllow.setChecked(true);
    }
}