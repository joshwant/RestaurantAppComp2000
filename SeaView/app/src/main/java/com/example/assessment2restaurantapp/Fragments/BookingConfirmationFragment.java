package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assessment2restaurantapp.R;


public class BookingConfirmationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public BookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static BookingConfirmationFragment newInstance(String param1, String param2) {
        BookingConfirmationFragment fragment = new BookingConfirmationFragment();
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
        View view = inflater.inflate(R.layout.fragment_booking_confirmation, container, false);

        Button manageBooking = view.findViewById(R.id.manageBooking);
        manageBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageFragment ManageFragment = new ManageFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, ManageFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, new HomeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}