package de.kdsoftworx.ls_babyalbum2.Registry;


/**
 * Created by Kai on 07.07.2020 by the sweat of one´s brow.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {

    private static final String SIGNUP_REQUEST_URL = "http://steps.ddnss.eu/SignUp.php";
    private Map<String, String> params;

    public SignUpRequest(String username, String password, String email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, SIGNUP_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

