package de.kdsoftworx.ls_babyalbum2.Registry;


/**
 * Created by Kai on 04.09.2018 by the sweat of one´s brow.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://steps.ddnss.eu/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String useridsm, String username, String password,String salutation, String name, String surname, String dateOfBirth, String street, String housenumber, String postCode, String location, String email, String phone, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

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

