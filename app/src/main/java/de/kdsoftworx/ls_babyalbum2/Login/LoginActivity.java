package de.kdsoftworx.ls_babyalbum2.Login;


import android.Manifest;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.kdsoftworx.ls_babyalbum2.Activity.AccountActivity;
import de.kdsoftworx.ls_babyalbum2.Activity.ChildDataActivity;
import de.kdsoftworx.ls_babyalbum2.Data.User;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Helper.CShowProgress;
import de.kdsoftworx.ls_babyalbum2.OnBoarding.IntroActivity;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.Registry.RegisterActivity;
import de.kdsoftworx.ls_babyalbum2.Registry.SignupActivity;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {

    Button bLogin, bRegistry;
    EditText etUsername, etPassword;
    TextInputLayout username_TIL, password_TIL;

    // Facebook Login
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    // Local userdata and Database
    UserLokalStore userLokalStore;
    private BookdataViewModel bookdataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        username_TIL = findViewById(R.id.username_text_input_login);
        password_TIL = findViewById(R.id.password_text_input_login);

        bLogin = (Button) findViewById(R.id.bLogin);
        bRegistry = (Button) findViewById(R.id.bRegistry);

        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        userLokalStore = UserLokalStore.getInstance(getApplicationContext());

        // --------------- Config Goolge Sing In --------------------------------------------------------------
        // Configure Google sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //-------------------------------------------------------------------------------------------------------

        // OnClickListener for Login Button ----------------------------------------------------------------------
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Initialize Progress Bar
                final CShowProgress cShowProgress = CShowProgress.getInstance();
                //--------------------------------------------------------------------------------------------------

                final String username = username_TIL.getEditText().getText().toString();
                final String password = password_TIL.getEditText().getText().toString();

                if (TextUtils.isEmpty(username)) {
                    username_TIL.setError(getString(R.string.e_EmptyEditText));
                    return;
                } else {
                    if (TextUtils.isEmpty(password)) {
                        password_TIL.setError(getString(R.string.e_EmptyEditText));

                        //Delete Error-Message
                        username_TIL.setError(null);
                        return;
                    } else {

                        //Delete Error-Message
                        password_TIL.setError(null);

                        //Implement Responselistener if Request is done we get a response ---------------------------
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        // Get all User-Data from Response

                                        Integer userID = jsonResponse.getInt("userID");
                                        String useridsm = jsonResponse.getString("useridsm");
                                        String username = jsonResponse.getString("username");
                                        String password = jsonResponse.getString("password");
                                        String salutation = jsonResponse.getString("salutation");
                                        String name = jsonResponse.getString("name");
                                        String surname = jsonResponse.getString("surname");
                                        String dateOfBirth = jsonResponse.getString("dateOfBirth");
                                        String street = jsonResponse.getString("street");
                                        String houseNumber = jsonResponse.getString("houseNumber");
                                        String postCode = jsonResponse.getString("postCode");
                                        String location = jsonResponse.getString("location");
                                        String email = jsonResponse.getString("email");
                                        String phone = jsonResponse.getString("phone");

                                        // Delete entries
                                        etUsername.setText("");
                                        etPassword.setText("");

                                        // Store User-Data on Device
                                        User user = new User(userID, useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth, houseNumber, postCode, phone);

                                        userLokalStore.storeUserData(user);
                                        // Set User as "logged in"
                                        userLokalStore.setUserLoggedIn(true);
                                        // Set Intro Ststus: If User watched Intro -> 1 = don´t show Intro :: if new User login -> 0 = show Intro
                                        userLokalStore.setIntroStatus(false);

                                        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                        //dismiss Progress Bar
                                        cShowProgress.hideProgress();


                                        // Go to User-Area
                                        Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                    } else {

                                        //dismiss Progress Bar
                                        cShowProgress.hideProgress();
                                        // Show an Alert Dialog, that the Login was not successful!
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                                        alertBuilder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                                        return;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //-------------------------------------------------------------------------------------------
                        // Implement a ErrorListener if Request causes an Error -------------------------------------
                        final Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                //dismiss Progress Bar
                                cShowProgress.hideProgress();

                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        };

                        //-------------------------------------------------------------------------------------------------
                        //Set Login-Data to a Login-Request and send request to Database to get Userdata ------------------
                        LoginRequest loginRequest = new LoginRequest(username, password, responseListener, errorListener);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(loginRequest);
                        //--------------------------------------------------------------------------------------------------
                        // Show Progress Bar -------------------------------------------------------------------------------
                        cShowProgress.showProgress(LoginActivity.this);
                        //--------------------------------------------------------------------------------------------------


                    }
                }
            }
        });
        //--------------------------------------------------------------------------------------------------------

        // ------------ Facebook Login --------------------------------------------------------------
        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();


        loginButton.setPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setLoginText(getString(R.string.fb_login_button_text));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                userLokalStore.saveAccessToken(accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Bundle bundle = getFacebookData(object, userLokalStore);

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        // ------------ Google Login --------------------------------------------------------------
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.sign_in_button)
                {
                    signIn(mGoogleSignInClient);
                }
            }
        });
        // ------------------------------------------------------------------------------------------


        // OnClickListener for Register Button -------------------------------------------------------------------
        bRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(signupIntent);
            }
        });
        //--------------------------------------------------------------------------------------------------------
    }

    // ------------------------ FACEBOOK  --------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Pass the result to Facebook Login callbackManager
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Get Result from Google and pass it to Method getGoogleData
        if (requestCode == 1000) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            getGoogleData(task);
        }

    }

    // Method to get Facebook Data
    private Bundle getFacebookData(final JSONObject jsonObject, final UserLokalStore userLokalStore) {

        final Bundle bundle = new Bundle();
        // Initialize Progress Bar
        final CShowProgress cShowProgress = CShowProgress.getInstance();

        try {
            final String password = "";
            final String street = "";
            final String location = "";
            final String dateOfBirth = "";
            final String houseNumber = "";
            final String postCode = "";
            final String phone = "";
            final String salutation = "";

            final String useridsm = jsonObject.getString("id");
            final String username = jsonObject.getString("name");
            final String name = jsonObject.getString("first_name");
            final String surname = jsonObject.getString("last_name");
            final String email = jsonObject.getString("email");
            //final String gender = jsonObject.getString("gender");


            //Implement Responselistener if Request is done we get a response ---------------------------
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        boolean userdetect = jsonResponse.getBoolean("userdetect");

                        if (success) {
                            // Get all User-Data from Response
                            if (userdetect) {

                                Integer userID = jsonResponse.getInt("userID");
                                String useridsm = jsonResponse.getString("userIDSM");
                                String username = jsonResponse.getString("username");
                                String password = jsonResponse.getString("password");
                                String salutation = jsonResponse.getString("salutation");
                                String name = jsonResponse.getString("name");
                                String surname = jsonResponse.getString("surname");
                                String dateOfBirth = jsonResponse.getString("dateOfBirth");
                                String street = jsonResponse.getString("street");
                                String houseNumber = jsonResponse.getString("houseNumber");
                                String postCode = jsonResponse.getString("postCode");
                                String location = jsonResponse.getString("location");
                                String email = jsonResponse.getString("email");
                                String phone = jsonResponse.getString("phone");


                                // Store User-Data on Device
                                User user = new User(userID, useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth, houseNumber, postCode, phone);

                                userLokalStore.storeUserData(user);
                                // Set User as "logged in"
                                userLokalStore.setUserLoggedIn(true);
                                // Set Intro Ststus: If User watched Intro -> 1 = don´t show Intro :: if new User login -> 0 = show Intro
                                userLokalStore.setIntroStatus(false);

                                //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                //dismiss Progress Bar
                                cShowProgress.hideProgress();

                            } else {

                                Integer userID = jsonResponse.getInt("userID");

                                // Store User-Data on Device
                                User user = new User(userID, useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth, houseNumber, postCode, phone);

                                userLokalStore.storeUserData(user);
                                // Set User as "logged in"
                                userLokalStore.setUserLoggedIn(true);
                                // Set Intro Ststus: If User watched Intro -> 1 = don´t show Intro :: if new User login -> 0 = show Intro
                                userLokalStore.setIntroStatus(false);

                                //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                //dismiss Progress Bar
                                cShowProgress.hideProgress();
                            }

                            if (userLokalStore.getUserLoggedIn() && !userLokalStore.getLoggedInUser().getUseridsm().isEmpty()) {

                                // Go to User-Area
                                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                                LoginActivity.this.startActivity(intent);
                            }

                        } else {

                            //dismiss Progress Bar
                            cShowProgress.hideProgress();
                            // Show an Alert Dialog, that the Login was not successful!
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertBuilder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        //dismiss Progress Bar
                        cShowProgress.hideProgress();
                        Toast.makeText(getApplicationContext(), "ups! Response Facebook-Login Failed!" + e , Toast.LENGTH_LONG).show();

                    }
                }
            };
            //-------------------------------------------------------------------------------------------
            // Implement a ErrorListener if Request causes an Error -------------------------------------
            final Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //dismiss Progress Bar
                    cShowProgress.hideProgress();

                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            };
            //-------------------------------------------------------------------------------------------------
            //Set Login-Data to a Login-Request and send request to Database to get Userdata ------------------
            LoginRequestSozialMedia loginRequestSozialMedia = new LoginRequestSozialMedia(useridsm, username, password, salutation, name, surname, dateOfBirth, street, houseNumber, postCode, location, email, phone, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequestSozialMedia);
            //--------------------------------------------------------------------------------------------------
            // Show Progress Bar -------------------------------------------------------------------------------
            cShowProgress.showProgress(LoginActivity.this);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "1st. Try-Catch Failed! " + e, Toast.LENGTH_LONG).show();
        }

        return bundle;
    }
    // ------------------------------------------------------------------------------------------------

    // Method to get Data from Google
    private void signIn(GoogleSignInClient mGoogleSignInClient){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    // Method to get Data from Google Response
    private void getGoogleData (Task<GoogleSignInAccount> task){
        // Initialize Progress Bar
        final CShowProgress cShowProgress = CShowProgress.getInstance();

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Get Userdata
            final String password = "";
            final String street = "";
            final String location = "";
            final String dateOfBirth = "";
            final String houseNumber = "";
            final String postCode = "";
            final String phone = "";
            final String salutation = "";

            if (account != null) {
                final String useridsm = account.getId();
                final String username = account.getDisplayName();
                final String name = account.getGivenName();
                final String surname = account.getFamilyName();
                final String email = account.getEmail();

                // Store AccessToken
                userLokalStore.saveAccessToken(account.getIdToken());

                //Implement Responselistener if Request is done we get a response ---------------------------
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean userdetect = jsonResponse.getBoolean("userdetect");

                            if (success) {
                                // Get all User-Data from Response
                                if (userdetect) {

                                    Integer userID = jsonResponse.getInt("userID");
                                    String useridsm = jsonResponse.getString("userIDSM");
                                    String username = jsonResponse.getString("username");
                                    String password = jsonResponse.getString("password");
                                    String salutation = jsonResponse.getString("salutation");
                                    String name = jsonResponse.getString("name");
                                    String surname = jsonResponse.getString("surname");
                                    String dateOfBirth = jsonResponse.getString("dateOfBirth");
                                    String street = jsonResponse.getString("street");
                                    String houseNumber = jsonResponse.getString("houseNumber");
                                    String postCode = jsonResponse.getString("postCode");
                                    String location = jsonResponse.getString("location");
                                    String email = jsonResponse.getString("email");
                                    String phone = jsonResponse.getString("phone");


                                    // Store User-Data on Device
                                    User user = new User(userID, useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth, houseNumber, postCode, phone);

                                    userLokalStore.storeUserData(user);
                                    // Set User as "logged in"
                                    userLokalStore.setUserLoggedIn(true);
                                    // Set Intro Ststus: If User watched Intro -> 1 = don´t show Intro :: if new User login -> 0 = show Intro
                                    userLokalStore.setIntroStatus(false);

                                    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                    //dismiss Progress Bar
                                    cShowProgress.hideProgress();

                                } else {

                                    Integer userID = jsonResponse.getInt("userID");

                                    // Store User-Data on Device
                                    User user = new User(userID, useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth, houseNumber, postCode, phone);

                                    userLokalStore.storeUserData(user);
                                    // Set User as "logged in"
                                    userLokalStore.setUserLoggedIn(true);
                                    // Set Intro Ststus: If User watched Intro -> 1 = don´t show Intro :: if new User login -> 0 = show Intro
                                    userLokalStore.setIntroStatus(false);

                                    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                                    //dismiss Progress Bar
                                    cShowProgress.hideProgress();
                                }

                                if (userLokalStore.getUserLoggedIn() && !userLokalStore.getLoggedInUser().getUseridsm().isEmpty()) {

                                    // Go to User-Area
                                    Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                                    LoginActivity.this.startActivity(intent);
                                }

                            } else {

                                //dismiss Progress Bar
                                cShowProgress.hideProgress();
                                // Show an Alert Dialog, that the Login was not successful!
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertBuilder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            //dismiss Progress Bar
                            cShowProgress.hideProgress();
                            Toast.makeText(getApplicationContext(), "ups! Response Google-Login Failed!" + e, Toast.LENGTH_LONG).show();

                        }
                    }
                };
                //-------------------------------------------------------------------------------------------
                // Implement a ErrorListener if Request causes an Error -------------------------------------
                final Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //dismiss Progress Bar
                        cShowProgress.hideProgress();

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                };
                //-------------------------------------------------------------------------------------------------
                //Set Login-Data to a Login-Request and send request to Database to get Userdata ------------------
                LoginRequestSozialMedia loginRequestSozialMedia = new LoginRequestSozialMedia(useridsm, username, password, salutation, name, surname, dateOfBirth, street, houseNumber, postCode, location, email, phone, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequestSozialMedia);
                //--------------------------------------------------------------------------------------------------
                // Show Progress Bar -------------------------------------------------------------------------------
                cShowProgress.showProgress(LoginActivity.this);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //dismiss Progress Bar
            cShowProgress.hideProgress();
            // Show an Alert Dialog, that the Login was not successful!
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertBuilder.setMessage("Login Failed: Fault " + e.getStatusCode()).setNegativeButton("Retry", null).create().show();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (authenticate() && userLokalStore.getCurrentRecordId() > 0) {
            startActivity(new Intent(this, ChildDataActivity.class));
        } else if (authenticate() && userLokalStore.getCurrentRecordId() == 0 && userLokalStore.getIntroStatus()) {
            startActivity(new Intent(this, AccountActivity.class));
        } else if (authenticate() && !userLokalStore.getIntroStatus()) {
            startActivity(new Intent(this, IntroActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // check if the user is logged in
    private boolean authenticate() {
        return userLokalStore.getUserLoggedIn();
    }

}
