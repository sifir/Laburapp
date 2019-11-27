package ar.com.sifir.laburapp.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ar.com.sifir.laburapp.BaseActivity;
import ar.com.sifir.laburapp.NodeDetailActivity;
import ar.com.sifir.laburapp.R;
import ar.com.sifir.laburapp.entities.User;

public class UserAdapter extends ArrayAdapter<String> {
    private BaseActivity ctx;
    private User[] users;
    private String nodeID;

    public UserAdapter(BaseActivity ctx, int nodeListItem, User[] users, String nodeID) {
        super(ctx, nodeListItem, toStringList(users));
        this.ctx = ctx;
        this.users = users;
        this.nodeID = nodeID;
    }

    private static List<String> toStringList(User[] users) {
        ArrayList<String> list = new ArrayList<>();
        for (User u : users) {
            list.add(u.getId());
        }
        return list;
    }

    public View getView(final int position, View converterView, ViewGroup parent) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        final User user = users[position];
        View item = inflater.inflate(R.layout.item_listado_users, null);

        TextView txt1 = item.findViewById(R.id.userNameTxt);
        txt1.setText(user.getFirstName());
        TextView txt2 = item.findViewById(R.id.userLastTxt);
        txt2.setText(user.getLastName());
        TextView txt3 = item.findViewById(R.id.mailTxt);
        txt3.setText(user.getEmail());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle(ctx.getString(R.string.dltUser))
                        .setMessage(ctx.getString(R.string.dltUserAsk))
                        .setPositiveButton(ctx.getString(R.string.acept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ctx.getHttpService().deleteUser(user, nodeID,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(ctx, R.string.dltedUser, Toast.LENGTH_SHORT).show();
                                                ctx.refresh();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("Error", String.valueOf(error));
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });
        return item;
    }
}
