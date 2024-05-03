package com.example.assessment2restaurantapp;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String userEmail;

    private Boolean APISuccessBool;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String enteredEmail) {
        this.userEmail = enteredEmail;
    }

    public Boolean getAPISuccessBool() {
        return APISuccessBool;
    }

    public void setAPISuccessBool(Boolean APISuccessBool) {
        this.APISuccessBool = APISuccessBool;
    }

}
