package ar.com.sifir.laburapp.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ar.com.sifir.laburapp.entities.Invite;

public class HttpService {

    private static final String SERVER_URL = "https://laburapp.herokuapp.com";
    //    private static final String SERVER_URL = "http://192.168.0.6:1337";
    private final RequestQueue queue;

    public HttpService(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void authenticate(JSONObject params, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                //url de login
                SERVER_URL + "/auth/local",
                params,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void nodesByAdmin(String admin, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        StringRequest request = new StringRequest(Request.Method.GET,
                SERVER_URL + "/nodes?administrator=" + admin,
                //1er callback - respuesta
                onSuccess,
                //2do callback - error
                onError);
        queue.add(request);
    }

    public void nodeById(String nodeID, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                SERVER_URL + "/nodes?id=" + nodeID,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void stamp(JSONObject obj, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                //url de login
                SERVER_URL + "/stamps",
                obj,
                onSuccess,
                onError
        );
        queue.add(request);
    }

    public void createNode(JSONObject params, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                //url de nodos
                SERVER_URL + "/nodes",
                params,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void createUser(JSONObject params, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                //url de login
                SERVER_URL + "/users",
                params,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void createInvite(Invite invite, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("node", invite.getNode());
        body.put("node", invite.getNode());
        body.put("node", invite.getNode());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SERVER_URL + "/invites",
                body,
                onSuccess,
                onError
        );
        queue.add(request);
    }

}
