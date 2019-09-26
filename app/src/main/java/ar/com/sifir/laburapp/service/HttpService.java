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
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;

public class HttpService {

    private static final String SERVER_URL = "https://laburapp.herokuapp.com";
    //    private static final String SERVER_URL = "http://192.168.0.6:1337";
    private final RequestQueue queue;

    public HttpService(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void authenticate(String username, String password, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        //autenticacion usuario
        JSONObject obj = new JSONObject();
        try {
            obj.put("identifier", username);
            obj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                //url de login
                SERVER_URL + "/auth/local",
                obj,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void nodesByAdmin(String adminId, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        StringRequest request = new StringRequest(Request.Method.GET,
                SERVER_URL + "/nodes?administrator=" + adminId,
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

    public void stamp(String userId, String tag, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", userId);
            obj.put("tag", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void createNode(Node node, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject body = new JSONObject();
        try {
            body.put("administrator", node.getAdministrador());
            body.put("name", node.getName());
            body.put("tag", node.getTag());
            body.put("shift_starts", Integer.valueOf(node.getShiftStarts()));
            body.put("shift_ends", Integer.valueOf(node.getShiftEnds()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                //url de nodos
                SERVER_URL + "/nodes",
                body,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void createUser(User user, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {

        JSONObject body = new JSONObject();
        try {
            body.put("firstName", user.getFirstName());
            body.put("lastName", user.getLastName());
            body.put("email", user.getEmail());
            body.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                //url de login
                SERVER_URL + "/users",
                body,
                onSuccess,
                onError);
        queue.add(request);
    }

    public void createInvite(Invite invite, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject body = new JSONObject();
        try {
            body.put("node", invite.getNode());
            body.put("node", invite.getNode());
            body.put("node", invite.getNode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
