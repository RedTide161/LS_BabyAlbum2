package de.kdsoftworx.ls_babyalbum2.Registry;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Helper.CShowProgress;
import de.kdsoftworx.ls_babyalbum2.Login.LoginActivity;
import de.kdsoftworx.ls_babyalbum2.R;

public class RegisterActivity extends AppCompatActivity {

    Button bRegister;

    String regSalutation = "";
    TextInputLayout username_TIL, password_TIL, confPassword_TIL, name_TIL, surname_TIL, dateOfBirth_TIL, street_TIL, houseNumber_TIL, postCode_TIL, location_TIL, email_TIL, phone_TIL;
    UserLokalStore userLokalStore;

    private String TAG = "RegisterActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get the Data from the Register-Form
        final EditText etRegUsername = findViewById(R.id.etRegUsername);
        final EditText etRegPassword = findViewById(R.id.etRegPassword);
        final EditText etRegConfPassword = findViewById(R.id.etRegConfPassword);
        final EditText etRegName = findViewById(R.id.etRegName);
        final EditText etRegSurname = findViewById(R.id.etRegSurname);
        final EditText etRegStreet = findViewById(R.id.etRegStreet);
        final EditText etRegHouseNumber = findViewById(R.id.etRegHouseNumber);
        final EditText etRegPostCode = findViewById(R.id.etRegPostcode);
        final EditText etRegLocation = findViewById(R.id.etRegLocation);
        final EditText etRegEmail = findViewById(R.id.etRegEmail);
        final EditText etRegPhone = findViewById(R.id.etRegPhone);

        final RadioButton rbSalutation = findViewById(R.id.rbRegFemale);
        final EditText etRegShowDatePicker = findViewById(R.id.etRegShowDatePicker);

        // get View for Errortexts
        username_TIL = findViewById(R.id.reg_username_text_input);
        password_TIL = findViewById(R.id.reg_password_text_input);
        confPassword_TIL = findViewById(R.id.reg_confPassword_text_input);
        name_TIL = findViewById(R.id.reg_name_text_input);
        surname_TIL = findViewById(R.id.reg_surname_text_input);
        dateOfBirth_TIL = findViewById(R.id.reg_dateOfBirth_text_input);
        street_TIL = findViewById(R.id.reg_street_text_input);
        houseNumber_TIL = findViewById(R.id.reg_houseNumer_text_input);
        postCode_TIL = findViewById(R.id.reg_postcode_text_input);
        location_TIL = findViewById(R.id.reg_location_text_input);
        email_TIL = findViewById(R.id.reg_email_text_input);
        phone_TIL = findViewById(R.id.reg_phone_text_input);

        // ----------------------------  DatePickerDialog  -----------------------------------------
        // OnClickListener for DatePickerDialog
        etRegShowDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create a Calender-Object
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH); // android.R.style.Theme_Holo_Light_Dialog

                // create DatePickerDialog Object
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog, mDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //create OnDateSetListener Object
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "." + month + "." + year;
                etRegShowDatePicker.setText(date);
            }
        };

        //----------------------------------------------------------------------------------------------------------------------------------------------------------


        // create a userLokalStor Objekt to Store Userdata on the Phone
        userLokalStore = UserLokalStore.getInstance(getApplicationContext());


        bRegister = findViewById(R.id.bRegister);
        // Clicklistener for Register-Button -> looking for Clicks on Button Register---------------------------------------------------------------------------------------
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Get the User Data out of the EditText
                final String username = etRegUsername.getText().toString();
                final String useridsm = "";
                final String password = etRegPassword.getText().toString();
                final String confPassword = etRegConfPassword.getText().toString();
                final String name = etRegName.getText().toString();
                final String surname = etRegSurname.getText().toString();
                final String street = etRegStreet.getText().toString();
                final String location = etRegLocation.getText().toString();
                final String email = etRegEmail.getText().toString();
                final String dateOfBirth = etRegShowDatePicker.getText().toString();
                final String houseNumber = etRegHouseNumber.getText().toString();
                final String postCode = etRegPostCode.getText().toString();
                final String phone =etRegPhone.getText().toString();

                // Initialize Progress Bar
                final CShowProgress cShowProgress = CShowProgress.getInstance();
                //--------------------------------------------------------------------------------------------------

                // check if Texfield is empty -> if yes --> Error
                if (TextUtils.isEmpty(username))
                {
                    username_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    username_TIL.setError(null);
                }
                if (TextUtils.isEmpty(password))
                {
                    password_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    password_TIL.setError(null);
                }
                if (TextUtils.isEmpty(confPassword))
                {
                    confPassword_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    confPassword_TIL.setError(null);
                }
                if (TextUtils.isEmpty(name))
                {
                    name_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    name_TIL.setError(null);
                }
                if (TextUtils.isEmpty(surname))
                {
                    surname_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    surname_TIL.setError(null);
                }
                if (TextUtils.isEmpty(street))
                {
                    street_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    street_TIL.setError(null);
                }
                if (TextUtils.isEmpty(location))
                {
                    location_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    location_TIL.setError(null);
                }
                if (TextUtils.isEmpty(email))
                {
                    email_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    email_TIL.setError(null);
                }
                if (TextUtils.isEmpty(dateOfBirth))
                {
                    dateOfBirth_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    dateOfBirth_TIL.setError(null);
                }
                if (TextUtils.isEmpty(houseNumber))
                {
                    houseNumber_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    houseNumber_TIL.setError(null);
                }
                if (TextUtils.isEmpty(postCode))
                {
                    postCode_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    postCode_TIL.setError(null);
                }
                if (TextUtils.isEmpty(phone))
                {
                    phone_TIL.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    phone_TIL.setError(null);
                }
                if (TextUtils.isEmpty(regSalutation))
                {
                    rbSalutation.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    rbSalutation.setError(null);
                }

                //check for same passwords
                if (password.equals(confPassword)) {

                    //Implement Responselistener if Request is done we get a response
                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {
                                // create a new JSONObjekt
                                JSONObject jsonResponse = new JSONObject(response);
                                // get the value of the Resposne-Variable "success"
                                boolean success = false;
                                boolean userdetect = false;


                                success = jsonResponse.getBoolean("success");
                                userdetect = jsonResponse.getBoolean("userdetect");
                                Integer userID = jsonResponse.getInt("userid");

                                // check if username is already used
                                if (userdetect)
                                {
                                    //dismiss Progress Bar
                                    cShowProgress.hideProgress();

                                    Toast.makeText(getApplicationContext(),R.string.e_UserFound, Toast.LENGTH_LONG).show();
                                    etRegUsername.setError(getString(R.string.e_UserFound));
                                    return;
                                }
                                else
                                {
                                    if (success) {
                                        //dismiss Progress Bar
                                        cShowProgress.hideProgress();

                                        // Show Message: Registry successful
                                        Toast.makeText(getApplicationContext(), R.string.m_RegistryOK, Toast.LENGTH_SHORT).show();

                                        Log.d(TAG,"Aktuelle User ID: "+ userID);

                                        // go further to page Selection
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        RegisterActivity.this.startActivity(intent);
                                    }
                                    else {

                                        //dismiss Progress Bar
                                        cShowProgress.hideProgress();
                                        // Show an Alert Dialog, that the Registry was not successful!
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
                                        alertBuilder.setMessage("Registry Failed").setNegativeButton("Retry", null).create().show();
                                    }
                                }



                            } catch (JSONException e) {
                                //dismiss Progress Bar
                                cShowProgress.hideProgress();

                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),"JSONExpect", Toast.LENGTH_LONG).show();
                            }

                        }
                    };

                    //implement Error-Listener -> only to show the Registry Error! Later this could be deleted?!?
                    final Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                        }
                    };


                    //Set Register-Data to a Register-Request and send request to Database to store Userdata
                    RegisterRequest registerRequest = new RegisterRequest(useridsm,username,password,regSalutation,name, surname, dateOfBirth,street, houseNumber,postCode,location,email,phone, responseListener,errorListener );
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                    // Show Progress Bar -------------------------------------------------------------------------------
                    cShowProgress.showProgress(RegisterActivity.this);
                    //--------------------------------------------------------------------------------------------------
                }
                else
                {
                    // Show Message: Passwords aren´t identical
                    Toast.makeText(getApplicationContext(),R.string.e_PasswordNotOK, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //---------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    public void onRadioButtonClicked (View view)
    {
        boolean rbChecked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rbRegMale:
                if(rbChecked){
                    regSalutation = "Herr";
                }
                break;
            case R.id.rbRegFemale:
                if (rbChecked){
                    regSalutation = "Frau";
                }
                break;
        }
    }

}

