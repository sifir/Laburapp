package ar.com.sifir.laburapp.adapters.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ar.com.sifir.laburapp.R;

public class InviteViewHolder extends RecyclerView.ViewHolder {

    public TextView nodeName;

    public TextView requesterEmail;

    public InviteViewHolder(View itemView) {
        super(itemView);
        nodeName = itemView.findViewById(R.id.invite_node_name);
        requesterEmail = itemView.findViewById(R.id.requesterTxt);
    }

}
