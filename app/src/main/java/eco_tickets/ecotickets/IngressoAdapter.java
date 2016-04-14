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

public class IngressoAdapter extends RecyclerView.Adapter<IngressoAdapter.MyViewHolder> {

    private List<Ingresso> ingressosList;
    private Realm realm;


    public IngressoAdapter(List<Ingresso> ingressos) {
        this.ingressosList = ingressos;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, documento;
        public CheckBox checked;

        public MyViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.txt_nome);
            documento = (TextView) view.findViewById(R.id.txt_documento);
            checked = (CheckBox) view.findViewById(R.id.cb_checkedIn);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingresso_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Ingresso ingresso = ingressosList.get(position);

        holder.nome.setText(ingresso.getNome());
        holder.documento.setText("Documento: " + ingresso.getDocumento());

        holder.checked.setOnCheckedChangeListener(null);
        holder.checked.setChecked((ingresso.isChecked()));

        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

// Salva o status alterado

                realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                ingresso.setChecked(isChecked);
                realm.commitTransaction();

                realm.close(); // Fecha o real

            }
        });
    }

    @Override
    public int getItemCount() {
        return ingressosList.size();
    }
}