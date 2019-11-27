package ar.com.sifir.laburapp.adapters;

import android.content.DialogInterface;
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

import ar.com.sifir.laburapp.BaseActivity;
import ar.com.sifir.laburapp.R;
import ar.com.sifir.laburapp.entities.Invite;

public class InviteAdapter extends ArrayAdapter<String> {
    private BaseActivity ctx;
    private Invite[] invites;

    public InviteAdapter(BaseActivity ctx, int layout, String[] ids, Invite[] invites) {
        super(ctx, layout, ids);
        this.ctx = ctx;
        this.invites = invites;
    }

    public View getView(final int position, View converterView, ViewGroup parent) {
        LayoutInflater inflater = ctx.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_listado_invites, parent, false);
        final Invite invite = invites[position];

        TextView txt1 = item.findViewById(R.id.invite_node_name);
        txt1.setText(invite.getNode().getName());

        TextView txt2 = item.findViewById(R.id.requesterTxt);
        txt2.setText(invite.getRequester().getEmail());

        //ON CLICK
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle(ctx.getString(R.string.invite))
                        .setMessage(ctx.getString(R.string.inviteAsk) + invite.getNode().getName())
                        .setPositiveButton(ctx.getString(R.string.acept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ctx.getHttpService().acceptInvite(invite,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(ctx, ctx.getString(R.string.inviteAccepted), Toast.LENGTH_SHORT).show();
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
                        .setNegativeButton(ctx.getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ctx.getHttpService().rejectInvite(invite,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(ctx, ctx.getString(R.string.inviteRejected), Toast.LENGTH_SHORT).show();
                                                ctx.refresh();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });
        return item;
    }
}
