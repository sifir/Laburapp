package ar.com.sifir.laburapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import ar.com.sifir.laburapp.adapters.UserAdapter;
import ar.com.sifir.laburapp.entities.Invite;
import ar.com.sifir.laburapp.entities.Node;
import ar.com.sifir.laburapp.entities.User;


public class NodeDetailActivity extends BaseActivity implements InviteDialogFragment.NoticeDialogListener, Response.Listener<JSONObject>, Response.ErrorListener {

    String nodeID;
    TextView name;
    Context ctx = this;
    TextView schedule;

    //https://laburapp.herokuapp.com/nodes?id=5d7eb9c42a12237a0c1335da
    Node node = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_detail);
        setTitle("Detalle de Nodo");

        name = (TextView) findViewById(R.id.nodeTitleTxt);
        schedule = (TextView) findViewById(R.id.nodeHorarioTxt);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nodeID = bundle.getString("ID");

        fetchNode();
    }

    @Override
    public void refresh() {
        super.refresh();
        fetchNode();
    }

    private void fetchNode() {
        getHttpService().nodeById(
                nodeID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        node = getGson().fromJson(response, Node.class);
                        Log.i("exito:", response);

                        String nombre = "";
                        nombre = node.getName();
                        String horario = "";
                        horario = node.getShiftStarts() + " : " + node.getShiftEnds();
                        User[] users = node.getUsers();
                        name.setText(nombre);
                        schedule.setText(horario);
                        UserAdapter adapter = new UserAdapter(NodeDetailActivity.this, R.layout.item_listado_users, users, nodeID);
                        ListView nodelist = (ListView) findViewById(R.id.usersList);
                        Log.i("Adapter: ", adapter.toString());
                        nodelist.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //si falla
                        Log.i("Error:", error.toString());
                    }
                }
        );
    }

    public void addUser(View v) {
        DialogFragment dialog = new InviteDialogFragment();
        dialog.show(getSupportFragmentManager(), "InviteDialogFragment");
    }

    public void deleteNodeBtn(View v) {

        new AlertDialog.Builder(ctx)
                .setTitle(this.getString(R.string.dltNode))
                .setMessage(this.getString(R.string.dltNodeQuestion))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getHttpService().deleteNode(node,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.nodeDeleted), Toast.LENGTH_SHORT).show();
                                        finish();
                                       // getApplicationContext().refresh();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("error de volley ", error.toString());
                                    }
                                });
                    }
                })
                .setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    @Override
    public void onDialogPositiveClick(String email) {
        final NodeDetailActivity self = this;

        getHttpService().userByEmail(email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        User[] users = getGson().fromJson(response, User[].class);
                        try {
                            getHttpService().createInvite(
                                    new Invite(node, mUser, users[0]),
                                    self,
                                    self
                            );
                            Toast.makeText(self, self.getString(R.string.inviteSend), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(self, self.getString(R.string.noUserForMail), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(self, self.getString(R.string.noUserForMail), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Node detail activity", "error enviando invitacion");
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("Node detail activity", "invitacion enviada");
    }
}
