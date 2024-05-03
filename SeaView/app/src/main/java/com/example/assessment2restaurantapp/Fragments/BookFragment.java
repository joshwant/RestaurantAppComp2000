package com.example.assessment2restaurantapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessment2restaurantapp.R;
import com.example.assessment2restaurantapp.SharedViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BookFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EditText editTextDate;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private TextView guestCountTextView;
    private int guestCount = 1; //Initial guest count
    private  Button submitButton;
    private SharedViewModel sharedViewModel;
    private Boolean APISuccessBool;
    String customerName;


    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(String param1) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
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


    //onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);


        //Initialize views
        TextInputLayout textInputLayoutDate = view.findViewById(R.id.textInputLayoutDate);
        editTextDate = view.findViewById(R.id.editTextDate);

        guestCountTextView = view.findViewById(R.id.guestCount);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        setUpGuestCounter(view);

        RadioGroup radioGroupMealTime = view.findViewById(R.id.radioGroupMealTime);
        RadioButton radioLunch = view.findViewById(R.id.radioLunch);
        radioLunch.setChecked(true);  //Set Lunch to default

        RadioGroup radioGroupLocation = view.findViewById(R.id.radioGroupLocation);
        RadioButton radioGarden = view.findViewById(R.id.radioGarden);
        radioGarden.setChecked(true);  //Set Garden Side to default

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        String userEmail = sharedViewModel.getUserEmail();
        customerName = userEmail;

        //Get user email from shared view model
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        submitButton = view.findViewById(R.id.submitBooking);
        //submitButton.setEnabled(false);
        submitButton.setVisibility(View.INVISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open AvailabilityFragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, new AvailabilityFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                //API Request
                //Get the selected Date
                String selectedDate = editTextDate.getText().toString();

                //Get the selected Meal Time
                RadioGroup radioGroupMealTime = view.findViewById(R.id.radioGroupMealTime);
                int selectedMealTimeId = radioGroupMealTime.getCheckedRadioButtonId();
                RadioButton selectedMealTimeRadioButton = view.findViewById(selectedMealTimeId);
                String mealTime = selectedMealTimeRadioButton.getText().toString();

                //Get the selected Seating Area
                RadioGroup radioGroupLocation = view.findViewById(R.id.radioGroupLocation);
                int selectedSeatingAreaId = radioGroupLocation.getCheckedRadioButtonId();
                RadioButton selectedSeatingAreaRadioButton = view.findViewById(selectedSeatingAreaId);
                String seatingArea = selectedSeatingAreaRadioButton.getText().toString();

                //Get the Phone Number
                EditText phoneNumberEditText = view.findViewById(R.id.phoneNumber);
                String phoneNumber = phoneNumberEditText.getText().toString();


                //Call the API Request with the final data
                apiRequest(selectedDate, guestCount, mealTime, seatingArea, phoneNumber, customerName);
            }
        });

        return view;
    }

    //Date
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTimeInMillis(selection);

                SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
                String selectedDate = sdf.format(selectedCalendar.getTime());

                if (isDateWithinNextWeek(selectedDate)) {
                    Toast.makeText(requireContext(), "Selected date cannot be within the next week. Reselect a date.", Toast.LENGTH_LONG).show();
                    //submitButton.setEnabled(false);
                    submitButton.setVisibility(View.INVISIBLE);
                } else if (isCurrentDate(selectedCalendar)) {
                    Toast.makeText(requireContext(), "Selected date cannot be within the next week. Reselect a date.", Toast.LENGTH_LONG).show();
                    //submitButton.setEnabled(false);
                    submitButton.setVisibility(View.INVISIBLE);
                }else
                {
                    editTextDate.setText(selectedDate);
                    //submitButton.setEnabled(true);
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });
        datePicker.show(requireFragmentManager(), "Date Picker");
    }
    private boolean isCurrentDate(Calendar selectedCalendar) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        // Compare year, month, and day
        return selectedCalendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                selectedCalendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                selectedCalendar.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isDateWithinNextWeek(String selectedDate) {
        try {
            Date date = dateFormatter.parse(selectedDate);

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            Calendar nextWeek = Calendar.getInstance();
            nextWeek.add(Calendar.DAY_OF_MONTH, 7);

            return date.after(currentDate.getTime()) && date.before(nextWeek.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Guest Counter
    private void setUpGuestCounter(View view) {
        Button decrementButton = view.findViewById(R.id.decrementButton);
        Button incrementButton = view.findViewById(R.id.incrementButton);

        //Set click listener for decrement button
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementGuestCount();
            }
        });

        //Set click listener for increment button
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementGuestCount();
            }
        });
    }

    private void decrementGuestCount() {
        if (guestCount > 1) {
            guestCount--;
            updateGuestCount();
        }
    }
    private void incrementGuestCount() {
        if (guestCount < 10) {
            guestCount++;
            updateGuestCount();
        }
    }
    private void updateGuestCount() {
        guestCountTextView.setText(String.valueOf(guestCount));
    }



    //API
    public void apiRequest(String date, int tableSize, String mealTime, String seatingArea, String phoneNumber, String userEmail) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://web.socem.plymouth.ac.uk/COMP2000/ReservationApi/api/Reservations";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("customerName", customerName);
            jsonRequest.put("customerPhoneNumber", phoneNumber);
            jsonRequest.put("meal", mealTime);
            jsonRequest.put("seatingArea", seatingArea);
            jsonRequest.put("tableSize", tableSize);
            jsonRequest.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("API Request", "Request JSON: " + jsonRequest.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(requireContext(), "Request successful", Toast.LENGTH_LONG).show();
                        //Set API Success Boolean to True in shared view model
                        sharedViewModel.setAPISuccessBool(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "Request failed", Toast.LENGTH_LONG).show();
                Log.e("API Request", "Error: " + error.toString());
                //Set API Success Boolean to False in shared view model
                sharedViewModel.setAPISuccessBool(false);
            }
        });


        //Add request to request queue
        queue.add(jsonObjectRequest);
    }

}