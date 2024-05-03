package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assessment2restaurantapp.R;

public class ReviewsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        //Change layout visibility

        Button toggleButton = view.findViewById(R.id.reviewButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View reviewLayout = view.findViewById(R.id.reviewLayout);
                View reviewCompleteLayout = view.findViewById(R.id.reviewCompleteLayout);

                if (reviewLayout.getVisibility() == View.VISIBLE) {
                    reviewLayout.setVisibility(View.GONE);
                    reviewCompleteLayout.setVisibility(View.VISIBLE);
                } else {
                    reviewLayout.setVisibility(View.VISIBLE);
                    reviewCompleteLayout.setVisibility(View.GONE);
                }
            }
        });

        //Go to home page

        Button homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, new HomeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}