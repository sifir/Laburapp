package ar.com.sifir.laburapp.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.com.sifir.laburapp.entities.Invite;
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;
import ar.com.sifir.laburapp.service.network.HttpResponse;
import ar.com.sifir.laburapp.service.network.RequestWIthHeaders;

public class HttpService {

    private static final String SERVER_URL = "https://laburapp.herokuapp.com";
//        private static final String SERVER_URL = "http://10.0.2.2:1337";
    private String session = "";
    private final RequestQueue queue;
    private Gson gson = new Gson();

    public HttpService(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void authenticate(String username, String password, final Response.Listener<User> onSuccess, Response.ErrorListener onError) {
        //autenticacion usuario
        RequestWIthHeaders<User> request = new RequestWIthHeaders<>(
                User.class,
                Request.Method.POST,
                SERVER_URL + "/auth/local",
                "{\"identifier\":\"" + username + "\",\"password\":\"" + password + "\"}",
                new Response.Listener<HttpResponse<User>>() {
                    @Override
                    public void onResponse(HttpResponse<User> response) {
                        session = response.getHeaders()
                                .get("set-cookie")
                                .split(";")[0];
                        onSuccess.onResponse(response.getBody());
                    }
                },
                onError
        );
        queue.add(request);
    }

    public void nodesByAdmin(String adminId, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        get("/nodes?administrator=" + adminId, onSuccess, onError);
    }

    public void nodeById(String nodeID, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        get("/nodes?id=" + nodeID + "&populate=users", onSuccess, onError);
    }

    public void userNodesByTag(final String tag, User user, final Response.Listener<Node> onSuccess, final Response.ErrorListener onError) {
        get("/users/" + user.getId() + "/uses?tag=" + tag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Node[] found = gson.fromJson(response, Node[].class);
                if (found.length == 0)
                    onError.onErrorResponse(new VolleyError());
                else
                    onSuccess.onResponse(found[0]);
            }
        }, onError);
    }

    public void userByEmail(String email, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        get("/users?email=" + email, onSuccess, onError);
    }

    public void lastShiftbyId(String userId, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        get("/users/" + userId + "/shifts?sort={\"started\":\"DESC\"}&limit=1",
                onSuccess, onError);
    }

    public void invitesById(String userID, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        get("/invites?requested=" + userID + "&populate=node,requester,requested",
                onSuccess, onError);
    }

    public void stamp(User user, Node node, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", user.getId());
            obj.put("tag", node.getTag());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/stamps", obj, onSuccess, onError);
    }

    public void createNode(Node node, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject body = new JSONObject();
        try {
            body.put("administrator", node.getAdministrador());
            body.put("name", node.getName());
            body.put("tag", node.getTag());
            body.put("geolocation", node.getLocation());
            body.put("shift_starts", Integer.valueOf(node.getShiftStarts()));
            body.put("shift_ends", Integer.valueOf(node.getShiftEnds()));
            body.put("dias", toJsonArray(node.getDays()));
            body.put("isGPSenabled", node.isGPSenabled());
            body.put("isFingerEnabled", node.isFingerEnabled());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/nodes", body, onSuccess, onError);
    }

    public void deleteNode(Node node, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onErrorListener) {
        delete("/nodes/" + node.getId(), onSuccess, onErrorListener);
    }

    public void deleteUser(User user, String nodeID, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onErrorListener) {
        delete("/nodes/" + nodeID + "/users/" + user.getId(), onSuccess, onErrorListener);
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
        post("/users", body, onSuccess, onError);
    }

    public void acceptInvite(Invite invite, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        //AGREGO USER AL NODO
        rejectInvite(invite, new NoActionResponse<JSONObject>(), new NoActionResponse<>());
        post("/nodes/" + invite.getNode().getId() + "/users/" + invite.getRequested().getId(),
                null,
                onSuccess,
                onError);
    }

    public void rejectInvite(Invite invite, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onErrorListener) {
        delete("/invites/" + invite.getId(), onSuccess, onErrorListener);
    }

    public void createInvite(Invite invite, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        JSONObject body = new JSONObject();
        try {
            body.put("node", invite.getNode().getId());
            body.put("requester", invite.getRequester().getId());
            body.put("requested", invite.getRequested().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post("/invites", body, onSuccess, onError);
    }

    private void post(String url, JSONObject body, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        Log.d("HttpService", "POST to " + SERVER_URL + url + " with body " + body);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SERVER_URL + url,
                body,
                onSuccess,
                onError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };
        queue.add(request);
    }

    private void delete(String url, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onError) {
        Log.d("HttpService", "DELETE to " + SERVER_URL + url);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                SERVER_URL + url,
                null,
                onSuccess,
                onError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };
        queue.add(request);
    }

    private void get(String url, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        Log.d("HttpService", "GET to " + SERVER_URL + url);
        StringRequest request = new StringRequest(Request.Method.GET,
                SERVER_URL + url,
                onSuccess,
                onError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };
        queue.add(request);
    }

    private JSONArray toJsonArray(ArrayList<Boolean> values) {
        JSONArray array = new JSONArray();
        for (Object value : values) {
            array.put(value);
        }
        return array;
    }

    @NotNull
    private Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", session);
        return headers;
    }

    class NoActionResponse<T> implements Response.Listener<T>, Response.ErrorListener {


        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("NoActionResponse", String.valueOf(error));
        }

        @Override
        public void onResponse(T response) {
            Log.d("NoActionResponse", String.valueOf(response));
        }
    }

}
