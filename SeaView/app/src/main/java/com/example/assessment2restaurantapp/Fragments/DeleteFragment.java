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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessment2restaurantapp.R;
import com.example.assessment2restaurantapp.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeleteFragment extends Fragment {

    private ListView deleteBookingListView;
    private List<String> deleteBookingList;
    private Button deleteButton;
    private String lastBookingId;
    private SharedViewModel sharedViewModel;

    public DeleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

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

        deleteBookingListView = view.findViewById(R.id.deleteBookingListView);
        deleteBookingList = new ArrayList<>();
        deleteButton = view.findViewById(R.id.deleteButton);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // Add this line

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastBooking();
            }
        });
        getDeleteBooking();



        return view;
    }

    private void deleteLastBooking() {
        String url = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations";

        if (lastBookingId != null && !lastBookingId.isEmpty()) {
            RequestQueue queue = Volley.newRequestQueue(requireContext());

            StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url + "/" + lastBookingId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(requireContext(), "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle deletion error
                            Toast.makeText(requireContext(), "Error deleting booking", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(deleteRequest);
        } else {
            Toast.makeText(requireContext(), "Last booking ID is not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDeleteBooking() {
        String apiUrl = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processDeleteBookingResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error retrieving booking", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void processDeleteBookingResponse(JSONArray response) {
        try {
            String userEmail = sharedViewModel.getUserEmail(); //Retrieve userEmail from the SharedViewModel
            String lastBookingInfo = null;

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

                    lastBookingInfo = "Customer: " + customerName +
                            "\nDate: " + date +
                            "\nMeal Time: " + mealTime +
                            "\nSeating Area: " + seatingArea +
                            "\nGuests: " + tableSize;

                    lastBookingId = bookingObject.getString("id"); // Update lastBookingId
                    break; //Break out of loop once the last matching booking has been found
                }
            }

            deleteBookingList.clear(); //Clear the previous list
            if (lastBookingInfo != null) {
                deleteBookingList.add(lastBookingInfo);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, deleteBookingList);
            deleteBookingListView.setAdapter(arrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error processing booking", Toast.LENGTH_SHORT).show();
        }
    }
}
