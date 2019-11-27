package ar.com.sifir.laburapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.List;

import ar.com.sifir.laburapp.adapters.InvitesListAdapter;
import ar.com.sifir.laburapp.entities.Invite;

/**
 * Created by Sifir on 27/11/2017.
 */

public class InvitesMenuActivity extends BaseActivity implements Response.Listener<String>, Response.ErrorListener {

    RecyclerView mRecyclerView;

    InvitesListAdapter mInvitesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites_menu);
        setTitle("Invitaciones Pendientes");

        mRecyclerView = (RecyclerView) findViewById(R.id.inviteList);

        mInvitesListAdapter = new InvitesListAdapter(this);
        mRecyclerView.setAdapter(mInvitesListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void refresh() {
        super.refresh();
        getHttpService().invitesById(mUser.getId(), this, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //si falla
        Log.i("Error:", error.toString());
    }

    @Override
    public void onResponse(String response) {
        List<Invite> invites = Arrays.asList(getGson().fromJson(response, Invite[].class));
        Log.i("exito:", response);

        mInvitesListAdapter.update(invites);
    }
}
