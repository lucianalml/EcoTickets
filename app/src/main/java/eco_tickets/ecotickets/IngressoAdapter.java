package eco_tickets.ecotickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;

public class IngressoAdapter extends RecyclerView.Adapter<IngressoAdapter.ViewHolder> {

    private List<Ingresso> ingressosList;
    private Realm realm;

    public IngressoAdapter(List<Ingresso> ingressos) {
        this.ingressosList = ingressos;
    }

    @Override
    public IngressoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingresso_list_item, parent, false);

        ViewHolder holder = new ViewHolder(v);

//        holder.nome.setOnClickListener(IngressoAdapter.this);
//        holder.nome.setTag(holder);

        return holder;

//        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nome.setText(ingressosList.get(position).getNome());
        holder.documento.setText("Documento: " + ingressosList.get(position).getDocumento());
//        holder.checked.setOnCheckedChangeListener(null);
        holder.checked.setChecked((ingressosList.get(position).isChecked()));
        holder.qrCode = (ingressosList.get(position).getQrCode());

        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

// Salva o status alterado //

                realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//                ingresso.setChecked(isChecked);
//                realm.commitTransaction();
                realm.close(); // Fecha o real

            }
        });

    }

    @Override
    public int getItemCount() {
        return ingressosList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, documento;
        public CheckBox checked;
        public String qrCode;

        public ViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.txt_nome);
            documento = (TextView) view.findViewById(R.id.txt_documento);
            checked = (CheckBox) view.findViewById(R.id.cb_checkedIn);

        }
    }

}