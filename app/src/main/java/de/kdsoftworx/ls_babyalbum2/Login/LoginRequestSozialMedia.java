package de.kdsoftworx.ls_babyalbum2.Login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Kai on 12.05.2020 by the sweat of one´s brow.
 */

public class LoginRequestSozialMedia extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://steps.ddnss.eu/Login_sm.php";
    private Map<String, String> params;

    public LoginRequestSozialMedia(String useridsm, String username, String password, String salutation, String name, String surname, String dateOfBirth, String street, String housenumber, String postCode, String location, String email, String phone, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();
        params.put("useridsm", useridsm);
        params.put("username", username);
        params.put("password", password);
        params.put("salutation", salutation);
        params.put("name", name);
        params.put("surname", surname);
        params.put("dateOfBirth", dateOfBirth);
        params.put("street", street);
        params.put("houseNumber", housenumber);
        params.put("postCode", postCode);
        params.put("location", location);
        params.put("email", email);
        params.put("phone", phone);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}

