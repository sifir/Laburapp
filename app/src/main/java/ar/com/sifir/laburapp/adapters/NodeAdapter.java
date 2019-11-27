package ar.com.sifir.laburapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ar.com.sifir.laburapp.NodeDetailActivity;
import ar.com.sifir.laburapp.R;

public class NodeAdapter extends ArrayAdapter<String> {
    private Activity ctx;
    String[] nombres;
    String[] ids;

    public NodeAdapter(Activity ctx, int nodeListItem, String[] nombres, String[] ids ){
        super(ctx,nodeListItem, nombres);
        this.ctx = ctx;
        this.nombres = nombres;
        this.ids = ids;
    }

    public View getView(final int position, View converterView, ViewGroup parent){
        LayoutInflater inflater = ctx.getLayoutInflater();
        View item= inflater.inflate(R.layout.item_listado_nodes, null);

        TextView txt1 = item.findViewById(R.id.nodeTxt);
        txt1.setText(nombres[position]);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //click en un nodo de la lista
                Intent i = new Intent(ctx, NodeDetailActivity.class);
                i.putExtra("ID", ids[position]);
                ctx.startActivity(i);
            }
        });

        return item;
    }
}
