package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.assessment2restaurantapp.R;
import com.example.assessment2restaurantapp.SharedViewModel;


public class AvailabilityFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayout tableContainer;
    private RadioButton lastCheckedRadioButton = null;
    private SharedViewModel sharedViewModel;
    private Boolean APISuccessBool;

    public AvailabilityFragment() {
        // Required empty public constructor
    }
    public static AvailabilityFragment newInstance(String param1, String param2) {
        AvailabilityFragment fragment = new AvailabilityFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_availability, container, false);
        tableContainer = view.findViewById(R.id.tableContainer);

        int maxTables = 6;

        for (int i = 1; i <= maxTables; i++) {
            addTable(i);
        }

        setDefaultSelection();

        //Get API Success Boolean from shared view model
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        APISuccessBool = sharedViewModel.getAPISuccessBool();


        Button bookTableButton = view.findViewById(R.id.completeBooking);
        bookTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(APISuccessBool = true){
                    BookingConfirmationFragment bookingConfirmationFragment = new BookingConfirmationFragment();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framelayout, bookingConfirmationFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    BookingFailedFragment bookingFailedFragment = new BookingFailedFragment();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framelayout, bookingFailedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }

    private void addTable(int tableNumber) {
        RadioButton radioButton = new RadioButton(requireContext());
        radioButton.setText("Table #" + tableNumber);
        radioButton.setTextSize(26);
        radioButton.setChecked(false);

        radioButton.setOnClickListener(v -> onRadioButtonClicked(radioButton));

        tableContainer.addView(radioButton);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 64);
        radioButton.setLayoutParams(layoutParams);
    }

    private void onRadioButtonClicked(RadioButton radioButton) {
        if (lastCheckedRadioButton != null) {
            lastCheckedRadioButton.setChecked(false);
        }
        radioButton.setChecked(true);
        lastCheckedRadioButton = radioButton;
    }
    private void setDefaultSelection() {
        RadioButton firstRadioButton = (RadioButton) tableContainer.getChildAt(0);
        if (firstRadioButton != null) {
            firstRadioButton.setChecked(true);
            lastCheckedRadioButton = firstRadioButton;
        }
    }


}
