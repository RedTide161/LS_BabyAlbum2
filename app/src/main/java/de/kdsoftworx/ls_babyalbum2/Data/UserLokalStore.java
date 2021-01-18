package de.kdsoftworx.ls_babyalbum2.Data;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import de.kdsoftworx.ls_babyalbum2.Data.User;

/**
 * Created by Kai on 30.08.2018 by the sweat of one´s brow.
 */

public class UserLokalStore {

    private final String SP_Name = "userdetail";
    private SharedPreferences userLokalDatabase;

    public static UserLokalStore instance;

    public static UserLokalStore getInstance (Context context)
    {
        if (instance == null) {//if there is no instance available... create new one

            // synchronize "getInstance-Method" that a second thread will have to wait until the "getInstance-Method" is completed for the first thread
            synchronized (UserLokalStore.class) {

                if (instance == null) { // double-check if there is a Instance available
                    instance = new UserLokalStore(context);
                }
            }
        }
        return instance;
    }

    private UserLokalStore (Context context)
    {
        userLokalDatabase = context.getSharedPreferences(SP_Name, 0);

    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putInt("userId", user.getUserId());
        spEditor.putString("useridsm", user.getUseridsm());
        spEditor.putString("username", user.getUsername());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("salutation", user.getSalutation());
        spEditor.putString("name", user.getName());
        spEditor.putString("surname", user.getSurname());
        spEditor.putString("street", user.getStreet());
        spEditor.putString("location", user.getLocation());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("dateOfBirth", user.getDateOfBirth());
        spEditor.putString("houseNumber", user.getHouseNumber());
        spEditor.putString("postCode", user.getPostCode());
        spEditor.putString("phone", user.getPhone());
        spEditor.apply();

    }

    // save Userdata on lokal Database
    public User getLoggedInUser ()
    {
        Integer userID = userLokalDatabase.getInt("userId", 0);
        String useridsm = userLokalDatabase.getString("useridsm", "");
        String username = userLokalDatabase.getString("username", "");
        String password = userLokalDatabase.getString("password", "");
        String salutation = userLokalDatabase.getString("salutation", "");
        String name = userLokalDatabase.getString("name", "");
        String surname = userLokalDatabase.getString("surname", "");
        String street = userLokalDatabase.getString("street", "");
        String location = userLokalDatabase.getString("location", "");
        String email = userLokalDatabase.getString("email", "");
        String dateOfBrith = userLokalDatabase.getString("dateOfBirth", "00000000");
        String houseNumber = userLokalDatabase.getString("houseNumber", "000");
        String postCode = userLokalDatabase.getString("postCode", "0000");
        String phone = userLokalDatabase.getString("phone", "000000000");

        User storedUser = new User(userID, useridsm, username,password, salutation, name, surname, street, location,email, dateOfBrith, houseNumber, postCode, phone);

        return storedUser;
    }

    // set user is logged in
    public void setUserLoggedIn (boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }

    // Check if user is logged in
    public boolean getUserLoggedIn ()
    {
        if (userLokalDatabase.getBoolean("loggedIn", false))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // set Current Record ID of ChildData
    public void setCurrentRecordId (Integer CRID){
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putInt("currentRecordId", CRID);
        spEditor.apply();
    }

    // get Current Record ID of ChildData
    public Integer getCurrentRecordId(){
        Integer CRID = userLokalDatabase.getInt("currentRecordId" , 0);

        return CRID;
    }

    // set Current Number of Pages to Userdata
    public void setCurrentNumberPages (Integer currentNumberPages)
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putInt("currentNumberPages", currentNumberPages);
        spEditor.apply();
    }

    // get Current Number of Pages from Userdata
    public Integer getCurrentNumberPages ()
    {

        Integer currentNumberPages = userLokalDatabase.getInt("currentNumberPages", 0);

        return currentNumberPages;
    }

    // set Current Page to Userdata
    public void setCurrentPage (Integer currentPage)
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putInt("currentPage", currentPage);
        spEditor.apply();
    }

    // get Current Page from Userdata
    public Integer getCurrentPage ()
    {

        Integer currentPage = userLokalDatabase.getInt("currentPage", 0);

        return currentPage;
    }

    // set Status Onboarding: true = don´t show Intro; false = show Intro
    public void setIntroStatus(Boolean status)
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putBoolean("introStatus", status);
        spEditor.apply();
    }

    // get Status Onboarding: true = don´t show Intro; false = show Intro
    public Boolean getIntroStatus ()
    {

        Boolean currentSatus = userLokalDatabase.getBoolean("introStatus", false);

        return currentSatus;
    }

    // ------------- FACEBOOK + GOOGLE LOGIN --------------------------------------------------------------

    public void saveAccessToken(String token) {

        SharedPreferences.Editor spEditor = userLokalDatabase.edit();

        spEditor.putString("fb_access_token", token);
        spEditor.apply();
    }


    public String getToken() {
        return userLokalDatabase.getString("fb_access_token", null);
    }

    // --------------------------------------------------------------------------------------------

    // Clear lokal Database from Userdata
    public void clearAllUserData ()
    {
        SharedPreferences.Editor spEditor = userLokalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }

}

