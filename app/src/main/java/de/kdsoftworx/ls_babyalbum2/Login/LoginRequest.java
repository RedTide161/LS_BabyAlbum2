package de.kdsoftworx.ls_babyalbum2.Login;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Kai on 02.10.2018 by the sweat of one´s brow.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://steps.ddnss.eu/Login.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}

