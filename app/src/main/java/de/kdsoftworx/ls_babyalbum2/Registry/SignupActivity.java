package de.kdsoftworx.ls_babyalbum2.Registry;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Helper.CShowProgress;
import de.kdsoftworx.ls_babyalbum2.Login.LoginActivity;
import de.kdsoftworx.ls_babyalbum2.R;


public class SignupActivity extends AppCompatActivity {

    Button signUpButton;
    TextInputLayout su_username_text_input, su_password_text_input, su_confPassword_text_input, su_email_text_input;
    UserLokalStore userLokalStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // get the Data from the Register-Form
        final EditText suUsername = findViewById(R.id.etSuUsername);
        final EditText suEmail = findViewById(R.id.etSuEmail);
        final EditText suPassword = findViewById(R.id.etSuPassword);
        final EditText suConfPassword = findViewById(R.id.etSuConfPassword);

        // get View for Errortexts
        su_username_text_input = findViewById(R.id.su_username_text_input);
        su_email_text_input = findViewById(R.id.su_email_text_input);
        su_password_text_input = findViewById(R.id.su_password_text_input);
        su_confPassword_text_input = findViewById(R.id.su_confPassword_text_input);

        // create a userLokalStor Objekt to Store Userdata on the Phone
        userLokalStore = UserLokalStore.getInstance(getApplicationContext());

        signUpButton = findViewById(R.id.bSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the User Data out of the EditText
                final String username = suUsername.getText().toString();
                final String password = suPassword.getText().toString();
                final String confPassword = suConfPassword.getText().toString();
                final String email = suEmail.getText().toString();

                // check if Texfield is empty -> if yes --> Error
                if (TextUtils.isEmpty(username))
                {
                    su_username_text_input.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    su_username_text_input.setError(null);
                }
                if (TextUtils.isEmpty(email))
                {
                    su_email_text_input.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    su_email_text_input.setError(null);
                }
                if (TextUtils.isEmpty(password))
                {
                    su_password_text_input.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    su_password_text_input.setError(null);
                }
                if (TextUtils.isEmpty(confPassword))
                {
                    su_confPassword_text_input.setError(getString(R.string.e_EmptyEditText));
                    Toast.makeText(getApplicationContext(),R.string.e_DataInclomplete, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    su_confPassword_text_input.setError(null);
                }

                // Register User
                signUpUser(username, email, password, confPassword);
            }
        });
    }

    private void signUpUser (String username, String email, String password, String confPassword ){

        // Initialize Progress Bar
        final CShowProgress cShowProgress = CShowProgress.getInstance();

        su_username_text_input = findViewById(R.id.su_username_text_input);

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


                        // check if username is already used
                        if (userdetect)
                        {
                            //dismiss Progress Bar
                            cShowProgress.hideProgress();

                            Toast.makeText(getApplicationContext(),R.string.e_UserFound, Toast.LENGTH_LONG).show();
                            su_username_text_input.setError(getString(R.string.e_UserFound));
                            return;
                        }
                        else
                        {
                            if (success) {
                                //dismiss Progress Bar
                                cShowProgress.hideProgress();

                                // Show Message: Registry successful
                                Toast.makeText(getApplicationContext(), R.string.m_RegistryOK, Toast.LENGTH_SHORT).show();

                                // go further to page Selection
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                SignupActivity.this.startActivity(intent);
                            }
                            else {

                                //dismiss Progress Bar
                                cShowProgress.hideProgress();
                                // Show an Alert Dialog, that the Registry was not successful!
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SignupActivity.this);
                                alertBuilder.setMessage("Registry Failed").setNegativeButton("Retry", null).create().show();
                            }
                        }



                    } catch (JSONException e) {
                        //dismiss Progress Bar
                        cShowProgress.hideProgress();

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"JSONExpect" + e, Toast.LENGTH_LONG).show();
                    }

                }
            };

            //implement Error-Listener -> only to show the Registry Error! Later this could be deleted?!?
            final Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //dismiss Progress Bar
                    cShowProgress.hideProgress();

                    Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                }
            };


            //Set Register-Data to a Register-Request and send request to Database to store Userdata
            SignUpRequest signUpRequest = new SignUpRequest(username,password,email, responseListener,errorListener );
            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
            queue.add(signUpRequest);
            // Show Progress Bar -------------------------------------------------------------------------------
            cShowProgress.showProgress(SignupActivity.this);
            //--------------------------------------------------------------------------------------------------
        }
        else
        {
            // Show Message: Passwords aren´t identical
            Toast.makeText(getApplicationContext(),R.string.e_PasswordNotOK, Toast.LENGTH_SHORT).show();
        }
    }
}
