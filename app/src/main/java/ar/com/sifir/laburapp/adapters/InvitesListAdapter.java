package ar.com.sifir.laburapp.adapters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

import ar.com.sifir.laburapp.BaseActivity;
import ar.com.sifir.laburapp.R;
import ar.com.sifir.laburapp.adapters.vh.InviteViewHolder;
import ar.com.sifir.laburapp.entities.Invite;

public class InvitesListAdapter extends RecyclerView.Adapter<InviteViewHolder> {

    private BaseActivity mContext;

    private List<Invite> mInvites;

    public InvitesListAdapter(BaseActivity context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_listado_invites, viewGroup, false);
        return new InviteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder inviteVH, int i) {
        final Invite invite = mInvites.get(i);

        inviteVH.nodeName.setText(invite.getNode().getName());
        inviteVH.requesterEmail.setText(invite.getRequester().getEmail());
        inviteVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.invite))
                        .setMessage(mContext.getString(R.string.inviteAsk) + invite.getNode().getName())
                        .setPositiveButton(mContext.getString(R.string.acept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mContext.getHttpService().acceptInvite(invite,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(mContext, mContext.getString(R.string.inviteAccepted), Toast.LENGTH_SHORT).show();
                                                mContext.refresh();
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
                        .setNegativeButton(mContext.getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mContext.getHttpService().rejectInvite(invite,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(mContext, mContext.getString(R.string.inviteRejected), Toast.LENGTH_SHORT).show();
                                                mContext.refresh();
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
    }

    @Override
    public int getItemCount() {
        return mInvites != null ? mInvites.size() : 0;
    }

    public void update(List<Invite> invites) {
        mInvites = invites;
        notifyDataSetChanged();
    }
}
