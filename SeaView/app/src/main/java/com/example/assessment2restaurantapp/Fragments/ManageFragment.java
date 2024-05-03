package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessment2restaurantapp.R;
import com.example.assessment2restaurantapp.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends Fragment {

    private ListView bookingListView;
    private List<String> bookingList;
    private Button editButton;
    private SharedViewModel sharedViewModel;


    public ManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        bookingListView = view.findViewById(R.id.bookingListView);
        bookingList = new ArrayList<>();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Add this line

        getBookings();


        editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, new DeleteFragment());
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

    //GET request
    private void getBookings() {
        String apiUrl = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processBookingResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error retrieving bookings", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void processBookingResponse(JSONArray response) {
        try {
            String userEmail = sharedViewModel.getUserEmail();

            //Start loop from the end of the array
            for (int i = response.length() - 1; i >= 0; i--) {
                JSONObject bookingObject = response.getJSONObject(i);

                String customerName = bookingObject.getString("customerName");

                //Check if customerName matches userEmail
                if (userEmail.equals(customerName)) {
                    String date = bookingObject.getString("date");
                    String mealTime = bookingObject.getString("meal");
                    String seatingArea = bookingObject.getString("seatingArea");
                    int tableSize = bookingObject.getInt("tableSize");

                    String bookingInfo = "Customer: " + customerName +
                            "\nDate: " + date +
                            "\nMeal Time: " + mealTime +
                            "\nSeating Area: " + seatingArea +
                            "\nGuests: " + tableSize;

                    //Clear previous list and add last matching booking
                    bookingList.clear();
                    bookingList.add(bookingInfo);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, bookingList);
                    bookingListView.setAdapter(arrayAdapter);

                    //Break out of loop once the last matching booking has been found
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error processing bookings", Toast.LENGTH_SHORT).show();
        }
    }
}
