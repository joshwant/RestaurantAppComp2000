package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private List<String> historyList;
    private SharedViewModel sharedViewModel;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        historyListView = view.findViewById(R.id.historyListView);
        historyList = new ArrayList<>();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        getHistoryBookings();

        return view;
    }

    // GET request for history bookings
    private void getHistoryBookings() {
        String apiUrl = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processHistoryBookingResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error retrieving history bookings", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    // Process and display history bookings
    private void processHistoryBookingResponse(JSONArray response) {
        try {
            String userEmail = sharedViewModel.getUserEmail();

            for (int i = 0; i < response.length(); i++) {
                JSONObject bookingObject = response.getJSONObject(i);

                String customerName = bookingObject.getString("customerName");

                // Check if customerName matches userEmail
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

                    historyList.add(bookingInfo);
                }
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, historyList);
            historyListView.setAdapter(arrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error processing history bookings", Toast.LENGTH_SHORT).show();
        }
    }
}
